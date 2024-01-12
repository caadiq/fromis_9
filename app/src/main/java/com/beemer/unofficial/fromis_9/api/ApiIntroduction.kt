package com.beemer.unofficial.fromis_9.api

import retrofit2.http.GET

interface ApiIntroduction {
    @GET("introduction")
    suspend fun getIntroduction(): IntroductionResponse
}

data class IntroductionResponse(
    val bannerImages: List<BannerImage>,
    val debutDate: DebutDate,
    val members: List<Member>
)

data class BannerImage(
    val imageUrl: String
)

data class DebutDate(
    val debutDate: String
)

data class Member(
    val name: String,
    val imageUrl: String,
    val birth: String,
    val position: String,
    val instagram: String
)