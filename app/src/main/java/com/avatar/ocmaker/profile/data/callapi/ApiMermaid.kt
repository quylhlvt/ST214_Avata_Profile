package com.avatar.ocmaker.profile.data.callapi

import com.avatar.ocmaker.profile.data.model.CharacterResponse
import retrofit2.http.GET

interface ApiMermaid {
    @GET("api/ST214_AvatarProfileMaker")
    suspend fun getAllData(): CharacterResponse
}