package com.zaphlabs.filechooser.filters

import java.io.File
import java.util.regex.Pattern

open class RegexFilter(
    regex: String,
    private val applyToFolders: Boolean = false,
    filter: Filter? = null
) : Filter(filter) {

    private val pattern = Pattern.compile(regex)

    override fun filter(file: File): Boolean {
        return if (applyToFolders || file.isFile) {
            pattern.matcher(file.name).matches()
        } else {
            true
        }
    }
}