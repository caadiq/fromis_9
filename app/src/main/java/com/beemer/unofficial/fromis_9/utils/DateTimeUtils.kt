package com.beemer.unofficial.fromis_9.utils

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object DateTimeUtils {
    // UTC -> KST 변환
    fun convertUTCtoKST(utcTime: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val dateTime = LocalDateTime.parse(utcTime, formatter).atOffset(ZoneOffset.UTC).plusHours(9).toLocalDate()
        return dateTime.toString().replace("-", ".")
    }
}