package com.ingsis.codeprocessing.redis.consumer

import com.ingsis.codeprocessing.redis.producer.ComplianceEnum
import com.ingsis.codeprocessing.redis.producer.LinterResponse
import com.ingsis.codeprocessing.redis.producer.LinterResponseProducer
import com.ingsis.codeprocessing.service.AssetService
import com.ingsis.codeprocessing.service.CodeProcessingService
import kotlinx.coroutines.runBlocking
import org.austral.ingsis.redis.RedisStreamConsumer
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component

@Component
class LinterRequestConsumer(
    @Value("\${redis.stream.request_linter_key}") streamKey: String,
    @Value("\${redis.groups.linter}") groupId: String,
    redis: RedisTemplate<String, String>,
    private val assetService: AssetService,
    private val codeProcessingService: CodeProcessingService,
    private val linterResponseProducer: LinterResponseProducer,
) : RedisStreamConsumer<LinterRequest>(streamKey, groupId, redis) {
    private val log = org.slf4j.LoggerFactory.getLogger(LinterRequestConsumer::class.java)

    override fun onMessage(record: ObjectRecord<String, LinterRequest>) {
        log.info("Consuming linter request event: ${record.value}")

        val snippetContent = assetService.getSnippet(record.value.snippet)
        if (!snippetContent.statusCode.is2xxSuccessful) {
            log.error("Error getting snippet with id: ${record.value.snippet}")
            return
        }

        try {
            log.info("Linting snippet: { id: ${record.value.snippet}, content: ${snippetContent.body} })")
            val linterResult =
                codeProcessingService.lintSnippet(
                    LinterRequest(
                        record.value.userId,
                        snippetContent.body!!,
                        record.value.linterRules,
                    ),
                )
            val complianceResult = if (linterResult.isEmpty()) ComplianceEnum.COMPLIANT else ComplianceEnum.NOT_COMPLIANT

            runBlocking {
                linterResponseProducer.publishEvent(
                    LinterResponse(
                        record.value.userId,
                        record.value.snippet,
                        complianceResult,
                    ),
                )
            }
        } catch (e: Exception) {
            log.error("Error while linting snippet: ${record.value.snippet}")
            runBlocking {
                linterResponseProducer.publishEvent(
                    LinterResponse(
                        record.value.userId,
                        record.value.snippet,
                        ComplianceEnum.FAILED,
                    ),
                )
            }
        }
    }

    override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, LinterRequest>> =
        StreamReceiver.StreamReceiverOptions
            .builder()
            .pollTimeout(java.time.Duration.ofMillis(10000))
            .targetType(LinterRequest::class.java)
            .build()
}
