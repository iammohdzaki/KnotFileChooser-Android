package com.zaphlabs.filechooser

import java.io.File

interface Sorter : Comparator<File> {

    object ByNameInAscendingOrder : Sorter {
        override fun compare(a: File, b: File) = a.name.compareTo(b.name, true)
    }

    object ByNameInDescendingOrder : Sorter {
        override fun compare(a: File, b: File) = -a.name.compareTo(b.name, true)
    }

    object ByNewestModification : Sorter {
        override fun compare(a: File, b: File) = -a.lastModified().compareTo(b.lastModified())
    }

    object ByLatestModification : Sorter {
        override fun compare(a: File, b: File) = a.lastModified().compareTo(b.lastModified())
    }

    object BySizeInAscendingOrder : Sorter {
        override fun compare(a: File, b: File) = a.length().compareTo(b.length())
    }

    object BySizeInDescendingOrder : Sorter {
        override fun compare(a: File, b: File) = -a.length().compareTo(b.length())
    }
}