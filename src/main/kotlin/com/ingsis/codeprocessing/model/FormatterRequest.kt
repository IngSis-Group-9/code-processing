package com.ingsis.codeprocessing.model

data class FormatterRequest(
    val snippet: String,
    val formatterRules: FormatterRulesDTO,
)

data class FormatterRulesDTO(
    val spaceBeforeColon: Boolean,
    val spaceAfterColon: Boolean,
    val spaceAroundAssignment: Boolean,
    val newlineBeforePrintln: Int,
    val nSpacesIndentationForIfStatement: Int,
)
