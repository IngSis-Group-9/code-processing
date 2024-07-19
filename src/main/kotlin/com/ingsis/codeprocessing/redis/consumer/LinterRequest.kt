package com.ingsis.codeprocessing.redis.consumer

data class LinterRequest(
    val userId: String,
    val snippet: String,
    val linterRules: LinterRulesDTO,
)

data class LinterRulesDTO(
    val printlnArgumentCheck: Boolean,
    val typeMatchingCheck: Boolean,
    val identifierNamingCheck: Boolean,
)
