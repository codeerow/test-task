package com.example.testtaskaura.infrastructure

import androidx.room.Room
import com.example.testtaskaura.core.BootRepository
import com.example.testtaskaura.infrastructure.persistence.AppDatabase
import com.example.testtaskaura.ui.MainViewModel
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
            klass = AppDatabase::class.java,
            name = "app_database",
        ).build()
    }

    single { get<AppDatabase>().bootInfoDao() }

    single { BootRepository(get()) }
}

val appModule = viewModelModule + dataModule
