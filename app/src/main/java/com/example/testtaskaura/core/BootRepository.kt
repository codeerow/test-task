package com.example.testtaskaura.core

import kotlinx.coroutines.flow.Flow

class BootRepository(
    private val bootInfoDao: BootInfoDao,
) {

    val allBootInfo: Flow<List<BootInfo>> = bootInfoDao.getAllBootInfo()

    suspend fun insert(bootInfo: BootInfo) {
        bootInfoDao.insert(bootInfo)
    }

    suspend fun getAllBootInfoSync(): List<BootInfo> {
        return bootInfoDao.getAllBootInfoSync()
    }
}
