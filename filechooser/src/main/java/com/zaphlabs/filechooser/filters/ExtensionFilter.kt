package com.zaphlabs.filechooser.filters

import com.zaphlabs.filechooser.utils.isFolder
import java.io.File

open class ExtensionFilter(
    filter: Filter? = null,
    private vararg val extensions: String
) : Filter() {

    override fun filter(file: File): Boolean {
        return if (file.isFolder) {
            true
        } else {
            extensions.forEach {
                if (it.compareTo(file.extension, true) == 0) return true
            }
            false
        }
    }
}