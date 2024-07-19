package com.ingsis.codeprocessing.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException(
    message: String,
) : RuntimeException(message)

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
class ServiceUnavailableException(
    message: String,
) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequestException(
    message: String,
) : RuntimeException(message)
