package com.ingsis.codeprocessing.service

import interpreter.inputs.ReadEnvSource

class MapReadEnvSource(
    private val envs: Map<String, String>,
) : ReadEnvSource {
    override fun readEnv(name: String): String? = envs[name]
}
