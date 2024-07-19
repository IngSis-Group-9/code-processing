package com.ingsis.codeprocessing.model

data class InterpreterResponse(
    val outputs: List<String>,
    val errors: List<String>,
)
