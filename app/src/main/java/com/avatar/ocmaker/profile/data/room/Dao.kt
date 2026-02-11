package com.avatar.ocmaker.profile.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.avatar.ocmaker.profile.data.model.AvatarModel


@Dao
interface Dao {

    @Insert
    fun addAvatar(callPhoneModel: AvatarModel): Long

    @Query("DELETE FROM AvatarModel WHERE path = :path ")
    fun deleteTheme(path: String,): Int

    @Query("SELECT * FROM AvatarModel WHERE path = :path")
    fun getTheme(path : String) : AvatarModel?
}