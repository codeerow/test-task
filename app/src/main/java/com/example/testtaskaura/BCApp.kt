package com.example.testtaskaura

import android.app.Application
import com.example.testtaskaura.infrastructure.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class BCApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BCApp)
            modules(appModule)
        }
    }
}