package com.example.testtaskaura.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.testtaskaura.R
import com.example.testtaskaura.core.BootNotificationWorker
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    private val textBootInfo by lazy { findViewById<TextView>(R.id.text_boot_info) } // TODO: remove with viewbinding


    // TODO: clean up
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val oneTimeWorkRequest = OneTimeWorkRequest.from(BootNotificationWorker::class.java)
        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)

        lifecycleScope.launch {
            viewModel.allBootInfo.collect { bootInfoList ->
                textBootInfo.text = bootInfoList.joinToString(separator = "\n") { bootInfo ->
                    "ID: ${bootInfo.id}, Date: ${bootInfo.date}"
                }
                bootInfoList.ifEmpty { textBootInfo.text = "No boots detected" }
            }
        }
    }
}