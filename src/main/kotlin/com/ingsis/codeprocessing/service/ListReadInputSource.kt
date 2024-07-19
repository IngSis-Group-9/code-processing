package com.ingsis.codeprocessing.service

import interpreter.inputs.ReadInputSource

class ListReadInputSource(
    private val inputs: List<String>,
) : ReadInputSource {
    private var index = 0

    override fun readInput(string: String): String? {
        if (index < inputs.size) {
            return inputs[index++]
        }
        return null
    }
}
