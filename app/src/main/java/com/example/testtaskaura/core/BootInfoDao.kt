package com.example.testtaskaura.core

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BootInfoDao {
    @Insert
    suspend fun insert(bootInfo: BootInfo)

    @Query("SELECT * FROM boot_info ORDER BY date DESC")
    fun getAllBootInfo(): Flow<List<BootInfo>>

    @Query("SELECT * FROM boot_info ORDER BY date DESC")
    suspend fun getAllBootInfoSync(): List<BootInfo>
}