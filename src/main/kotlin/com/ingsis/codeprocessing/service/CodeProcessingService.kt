package com.ingsis.codeprocessing.service

import com.ingsis.codeprocessing.exception.BadRequestException
import com.ingsis.codeprocessing.model.FormatterRequest
import com.ingsis.codeprocessing.model.InterpreterRequest
import com.ingsis.codeprocessing.model.InterpreterResponse
import com.ingsis.codeprocessing.redis.consumer.LinterRequest
import formatter.FormatRules
import interpreter.response.ErrorResponse
import interpreter.response.SuccessResponse
import org.springframework.stereotype.Service
import runner.PrintScriptRunner
import runner.PrintScriptVersion
import sca.StaticCodeAnalyzerRules
import sca.StaticCodeIssue
import java.io.ByteArrayInputStream

@Service
class CodeProcessingService {
    private val log = org.slf4j.LoggerFactory.getLogger(CodeProcessingService::class.java)
    private val snippetRunner = PrintScriptRunner()

    fun interpretSnippet(request: InterpreterRequest): InterpreterResponse {
        log.info("Interpreting code snippet ${request.snippet}")
        val snippetContent = ByteArrayInputStream(request.snippet.toByteArray())
        val readInputSource = ListReadInputSource(request.inputs)
        val readEnvSource = MapReadEnvSource(request.envs)

        return when (
            val response =
                snippetRunner.interpretSnippet(
                    snippetContent,
                    PrintScriptVersion.V2,
                    readInputSource,
                    readEnvSource,
                )
        ) {
            is SuccessResponse -> {
                log.info("Interpretation successful")
                InterpreterResponse(
                    response.message?.split("\n") ?: emptyList(),
                    emptyList(),
                )
            }
            is ErrorResponse -> {
                log.error("Error while interpreting code snippet: ${response.message}")
                InterpreterResponse(emptyList(), listOf(response.message))
            }
            else -> {
                log.error("Unknown response type")
                InterpreterResponse(emptyList(), listOf("Unknown response type"))
            }
        }
    }

    fun formatSnippet(request: FormatterRequest): String {
        log.info("Formatting code snippet ${request.snippet} with rules ${request.formatterRules}")
        val snippetContent = ByteArrayInputStream(request.snippet.toByteArray())
        val formatterRules =
            FormatRules(
                configFilePath = "",
                spaceBeforeColon = request.formatterRules.spaceBeforeColon,
                spaceAroundAssignment = request.formatterRules.spaceAroundAssignment,
                spaceAfterColon = request.formatterRules.spaceAfterColon,
                newlineBeforePrintln = request.formatterRules.newlineBeforePrintln,
                nSpacesIndentationForIfStatement = request.formatterRules.nSpacesIndentationForIfStatement,
            )

        try {
            return snippetRunner.formatSnippet(snippetContent, PrintScriptVersion.V2, formatterRules)
        } catch (e: Exception) {
            log.error("Error while formatting code snippet: ${e.message}")
            throw BadRequestException("Error while formatting code snippet: ${e.message}")
        } finally {
            log.info("Successfully formatted code snippet")
        }
    }

    fun lintSnippet(request: LinterRequest): List<StaticCodeIssue> {
        log.info("Linting code snippet ${request.snippet}")
        val snippetContent = ByteArrayInputStream(request.snippet.toByteArray())
        val scaRules =
            StaticCodeAnalyzerRules(
                configFilePath = "",
                typeMatchingCheck = request.linterRules.typeMatchingCheck,
                identifierNamingCheck = request.linterRules.identifierNamingCheck,
                functionArgumentCheck = request.linterRules.printlnArgumentCheck,
            )

        try {
            return snippetRunner.analyzeSnippet(snippetContent, PrintScriptVersion.V2, scaRules)
        } catch (e: Exception) {
            log.error("Error while linting code snippet: ${e.message}")
            throw BadRequestException("Error while linting code snippet: ${e.message}")
        } finally {
            log.info("Successfully linted code snippet")
        }
    }
}
