package com.ingsis.codeprocessing.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class RestBean {
    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()
}
