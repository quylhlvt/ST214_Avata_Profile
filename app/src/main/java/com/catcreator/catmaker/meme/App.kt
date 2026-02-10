package com.catcreator.catmaker.meme

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
@HiltAndroidApp
class App : Application()  {
    companion object {
        lateinit var instance:App
            private set
        val context: Context
            get() = instance.applicationContext
    }
    override fun onCreate() {
        super.onCreate()
        instance = this


    }

}