package com.ingsis.codeprocessing.controllers

import com.ingsis.codeprocessing.model.FileType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin("*")
@RestController
@RequestMapping("/fileTypes")
class FileTypeController {
    @GetMapping("/getTypes")
    fun getFileTypes(): List<FileType> {
        // Aquí puedes retornar los tipos de archivos que necesites.
        // Este es solo un ejemplo y deberías reemplazarlo con tu propia lógica.
        return listOf(
            FileType("printscript", "prs"),
        )
    }
}
