package com.example.testtaskaura.infrastructure.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testtaskaura.core.BootInfo
import com.example.testtaskaura.core.BootInfoDao
import com.example.testtaskaura.infrastructure.persistence.converter.DateConverter

@Database(entities = [BootInfo::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bootInfoDao(): BootInfoDao
}