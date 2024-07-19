package com.ingsis.codeprocessing.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class AssetService(
    private val rest: RestTemplate,
    @Value("\${azureBucket.url}") private val bucketUrl: String,
) {
    private val log = org.slf4j.LoggerFactory.getLogger(AssetService::class.java)

    fun getSnippet(id: String): ResponseEntity<String> {
        try {
            log.info("Getting snippet: { id: $id })")
            val headers = HttpHeaders()
            return rest.exchange("$bucketUrl/$id", HttpMethod.GET, HttpEntity<String>(headers), String::class.java)
        } catch (e: Exception) {
            log.error("Error getting snippet with id: $id", e)
            return ResponseEntity.badRequest().build()
        }
    }
}
