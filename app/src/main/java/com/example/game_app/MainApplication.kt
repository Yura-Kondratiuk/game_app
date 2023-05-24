package com.example.game_app

import android.app.Application
import mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(mainModule)
            androidContext(this@MainApplication)
        }
    }
}