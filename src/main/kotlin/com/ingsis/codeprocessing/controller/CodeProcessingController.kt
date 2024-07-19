package com.ingsis.codeprocessing.controller

import com.ingsis.codeprocessing.model.FormatterRequest
import com.ingsis.codeprocessing.model.InterpreterRequest
import com.ingsis.codeprocessing.model.InterpreterResponse
import com.ingsis.codeprocessing.service.CodeProcessingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/code-processing")
class CodeProcessingController(
    private val service: CodeProcessingService,
) {
    private val log = org.slf4j.LoggerFactory.getLogger(CodeProcessingController::class.java)

    @PostMapping("/interpret")
    fun interpret(
        @RequestBody input: InterpreterRequest,
    ): ResponseEntity<InterpreterResponse> {
        log.info("Interpreting code snippet: $input")
        return ResponseEntity.ok(service.interpretSnippet(input))
    }

    @PostMapping("/format")
    fun format(
        @RequestBody input: FormatterRequest,
    ): ResponseEntity<String> {
        log.info("Formatting code snippet: $input")
        return ResponseEntity.ok(service.formatSnippet(input))
    }
}
