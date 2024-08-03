package com.example.testtaskaura

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    private val textBootInfo by lazy { findViewById<TextView>(R.id.text_boot_info) } // TODO: remove with viewbinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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