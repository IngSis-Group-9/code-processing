package com.ingsis.codeprocessing.model

data class InterpreterRequest(
    val snippet: String,
    val inputs: List<String>,
    val envs: Map<String, String>,
)
