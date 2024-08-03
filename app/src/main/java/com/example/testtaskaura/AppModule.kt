package com.example.testtaskaura

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}

val dataModule = module {
    single {
        Room.databaseBuilder(
            context = androidApplication(),
            klass = BootDatabase::class.java,
            name = "boot_database",
        ).build()
    }

    single { get<BootDatabase>().bootInfoDao() }

    single { BootRepository(get()) }
}

val appModule = viewModelModule + dataModule
