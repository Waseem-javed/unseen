package com.example.unseenmessage

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class UtilsKeys {

    companion object {

        val WhatsAppKey = "Whatsapp"
        val FacebookKey = "Facebook"
        val InstagramKey = "Instagram"

        fun getCurentTime(): String {

            val date = Date()

            val formatTime = SimpleDateFormat("hh.mm aa")

            val time: String = formatTime.format(
                date
            )

            return time
        }

        fun getCurrentDate(): String {


            val currentDateTime =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime.now()
                } else {
                    TODO("VERSION.SDK_INT < O")
                }
            var date = currentDateTime.format(
                DateTimeFormatter.ofLocalizedDate(
                    FormatStyle.MEDIUM
                )
            )

            return date
        }

        fun getDateTime(): String {

            val dateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/y H:m:ss"))
            } else {
                TODO("VERSION.SDK_INT < O")
            }

            return dateTime
        }
    }
}