package com.sweetmay.taskmanager.view.ui

import android.app.Application
import com.sweetmay.taskmanager.di.appModule
import com.sweetmay.taskmanager.di.mainModule
import com.sweetmay.taskmanager.di.noteModule
import com.sweetmay.taskmanager.di.splashModule
import org.koin.android.ext.android.startKoin

class App: Application() {
    companion object {
        lateinit var instance : App
            private set
    }
    enum class Color {
        WHITE,
        VIOLET,
        YELLOW,
        RED,
        PINK,
        GREEN
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin(this, listOf(appModule, splashModule, noteModule, mainModule))
    }
}

