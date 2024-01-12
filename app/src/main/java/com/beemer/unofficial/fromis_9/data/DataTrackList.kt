package com.beemer.unofficial.fromis_9.data

data class DataTrackList(
    val albumName: String,
    val colorMain: String,
    val colorPrimary: String,
    val colorSecondary: String,
    val trackNumber: Int,
    val songName: String,
    val songLength: String,
    val titleTrack: Boolean,
    val videoId: String
)