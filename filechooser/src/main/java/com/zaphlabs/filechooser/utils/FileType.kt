package com.zaphlabs.filechooser.utils

/**
 * Developer : Mohammad Zaki
 * Created On : 14-03-2020
 */

enum class FileType {
    ALL,
    DOCUMENTS,
    AUDIO,
    VIDEO,
    IMAGE,
    DATABASE,
    CODE,
    APK
}

/**
 * Checks If the extension matches a specific file Type
 */
fun String.checkFileType(fileType: FileType):Boolean{
    when(fileType){
        FileType.ALL -> {
            return true
        }
        FileType.DOCUMENTS -> {
            if(this == "doc" || this == "pdf"){
                return true
            }
        }
        FileType.AUDIO -> {
            if(this == "mp3"){
                return true
            }
        }
        FileType.VIDEO -> {
            if(this == "mp4" || this == "mkv"){
                return true
            }
        }
        FileType.IMAGE -> {
            if(this == "png" || this == "jpeg" || this == "jpg"){
                return true
            }
        }
        FileType.DATABASE -> {
            if(this == "db"){
                return true
            }
        }
        FileType.CODE -> {
            if(this == "cpp" || this == "c" || this == "java" || this == "html"){
                return true
            }
        }
        FileType.APK ->{
            if(this == "apk"){
                return true
            }
        }
    }
    return false
}