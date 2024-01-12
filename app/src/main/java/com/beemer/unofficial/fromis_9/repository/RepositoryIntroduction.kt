package com.beemer.unofficial.fromis_9.repository

import com.beemer.unofficial.fromis_9.api.ApiIntroduction
import com.beemer.unofficial.fromis_9.api.IntroductionResponse

class RepositoryIntroduction(private val apiIntroduction: ApiIntroduction) {
    suspend fun getIntroduction(): IntroductionResponse {
        return apiIntroduction.getIntroduction()
    }
}