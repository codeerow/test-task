package com.example.testtaskaura.core

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "boot_info")
data class BootInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Date,
)