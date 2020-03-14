package com.zaphlabs.filechooser.preference

import android.content.Context
import java.io.File

/**
 * Developer : Mohammad Zaki
 * Created On : 14-03-2020
 */

object ChooserSharedPreference {
    private const val NAME = "knotfilechooser_prefs"
    private const val PREVIOUS_SELECTED_FOLDER = "prev_selected_folder"

    fun getPreviouslySelectedDirectory(context: Context, restoreFolder: Boolean, initialFolder: File): File {
        if (!restoreFolder) return initialFolder
        val path = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
            .getString(PREVIOUS_SELECTED_FOLDER, null)
        return if (path != null) File(path) else initialFolder
    }

    fun setPreviouslySelectedDirectory(context: Context, file: File) {
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(PREVIOUS_SELECTED_FOLDER, file.absolutePath)
            .apply()
    }
}