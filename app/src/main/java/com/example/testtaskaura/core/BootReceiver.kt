package com.example.testtaskaura.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Date

class BootReceiver : BroadcastReceiver(), KoinComponent {

    private val bootRepository: BootRepository by inject()

    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            insertBootInfo()
        }
    }

    private fun insertBootInfo() {
        val currentTime = System.currentTimeMillis()
        val bootInfo = BootInfo(date = Date(currentTime))

        CoroutineScope(Dispatchers.IO).launch {
            bootRepository.insert(bootInfo)
        }
    }
}