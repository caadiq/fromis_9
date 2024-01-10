package com.beemer.unofficial.fromis_9.data

data class DataSchedule(
    val time : String,
    val schedule : String,
    val description : String?,
    val url: String?,
    val image : String
)