package com.example.aleppocollage.util

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.paperdb.Paper

@HiltAndroidApp
class AleppoCollageApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Paper.init(this)
    }
}