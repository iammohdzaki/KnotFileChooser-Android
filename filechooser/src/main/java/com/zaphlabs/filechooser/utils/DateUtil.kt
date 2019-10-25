package com.zaphlabs.filechooser.utils

import java.util.*

/**
 * Developer : Mohammad Zaki
 * Created On : 25-10-2019
 */

object DateUtil{

    fun getRelativeTimeWithCurrentTime(time: Date): String {
        val currentDate = Calendar.getInstance().time
        val relativeDifference = (currentDate.time - time.time) / 1000

        return if (relativeDifference > 0) {
            when {
                relativeDifference < 60 -> "$relativeDifference seconds ago"
                relativeDifference < 60 * 60 -> (relativeDifference / 60).toString() + " minutes ago"
                relativeDifference < 60 * 60 * 24 -> (relativeDifference / (60 * 60)).toString() + " hours ago"
                else -> (relativeDifference / (60 * 60 * 24)).toString() + " days ago"
            }

        } else {
            "moments ago"
        }

    }
}
