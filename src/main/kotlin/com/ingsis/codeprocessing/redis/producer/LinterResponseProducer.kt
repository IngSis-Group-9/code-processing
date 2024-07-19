package com.ingsis.codeprocessing.redis.producer

import org.austral.ingsis.redis.RedisStreamProducer
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class LinterResponseProducer(
    @Value("\${redis.stream.response_linter_key}") streamKey: String,
    redis: RedisTemplate<String, String>,
) : RedisStreamProducer(streamKey, redis) {
    private val log = org.slf4j.LoggerFactory.getLogger(LinterResponseProducer::class.java)

    suspend fun publishEvent(event: LinterResponse) {
        log.info("Publishing event: $event")
        emit(event)
    }
}
