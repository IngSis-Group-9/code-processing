package com.ingsis.codeprocessing.redis.producer

data class LinterResponse(
    val userId: String,
    val snippetId: String,
    val linterResult: ComplianceEnum,
)

enum class ComplianceEnum {
    COMPLIANT,
    NOT_COMPLIANT,
    FAILED,
    PENDING,
}
