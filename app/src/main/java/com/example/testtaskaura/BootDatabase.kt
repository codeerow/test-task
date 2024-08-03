package com.example.testtaskaura

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [BootInfo::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BootDatabase : RoomDatabase() {
    abstract fun bootInfoDao(): BootInfoDao
}