package com.zaphlabs.filechooser.filters


import java.io.File
import java.io.FileFilter

abstract class Filter(private val filter: Filter? = null) : FileFilter {

    open fun filter(file: File): Boolean {
        return true
    }

    final override fun accept(file: File): Boolean {
        return filter(file) && (filter?.accept(file) ?: true)
    }
}