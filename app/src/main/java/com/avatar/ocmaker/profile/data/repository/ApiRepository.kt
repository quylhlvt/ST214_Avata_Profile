package com.avatar.ocmaker.profile.data.repository

import android.util.Log
import com.avatar.ocmaker.profile.data.callapi.ApiHelper
import com.avatar.ocmaker.profile.data.model.CharacterResponse
import com.avatar.ocmaker.profile.utils.CONST
import com.avatar.ocmaker.profile.utils.DataHelper
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiHelper: ApiHelper) {
    suspend fun getFigure(): CharacterResponse? {
        try {
            CONST.BASE_URL = CONST.BASE_URL_1
            return apiHelper.apiMermaid1.getAllData()
//            return null
        } catch (e: Exception) {
            Log.d(DataHelper.TAG, "getFigure: $e")
            try {
                CONST.BASE_URL = CONST.BASE_URL_2
//                return null
                return apiHelper.apiMermaid2.getAllData()
            } catch (e: Exception) {
                Log.d(DataHelper.TAG, "getFigure: $e")
                return null
            }
        }
    }
}