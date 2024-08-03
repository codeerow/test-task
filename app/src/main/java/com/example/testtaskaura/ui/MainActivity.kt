package com.example.testtaskaura.ui

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.testtaskaura.R
import com.example.testtaskaura.core.BootInfo
import com.example.testtaskaura.core.BootNotificationWorker
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

// TODO: clean up
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    // TODO: remove with viewbinding
    private val textBootInfo by lazy { findViewById<TextView>(R.id.text_boot_info) }
    private val inputTotalDismissals by lazy { findViewById<TextView>(R.id.input_total_dismissals) }
    private val inputIntervalBetweenDismissals by lazy { findViewById<TextView>(R.id.input_interval_between_dismissals) }
    private val buttonSaveSettings by lazy { findViewById<TextView>(R.id.button_save_settings) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadSettings()
        displayBootInfo()
        scheduleBootNotificationWorker()
        buttonSaveSettings.setOnClickListener {
            if (validateInputs()) {
                saveSettings()
            }
        }
    }

    private fun validateInputs(): Boolean {
        val totalDismissals = inputTotalDismissals.text.toString()
        val intervalBetweenDismissals = inputIntervalBetweenDismissals.text.toString()

        return when {
            totalDismissals.isEmpty() || intervalBetweenDismissals.isEmpty() -> {
                showToast("Fields cannot be empty.")
                false
            }

            totalDismissals.toIntOrNull() == null || intervalBetweenDismissals.toIntOrNull() == null -> {
                showToast("Please enter valid numbers.")
                false
            }

            totalDismissals.toInt() <= 0 || intervalBetweenDismissals.toInt() <= 0 -> {
                showToast("Values must be greater than 0.")
                false
            }

            totalDismissals.toLong() > Int.MAX_VALUE || intervalBetweenDismissals.toLong() > Int.MAX_VALUE -> {
                showToast("Values exceed maximum limit.")
                false
            }

            else -> true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun loadSettings() {
        val preferences = getSharedPreferences("dismiss_prefs", Context.MODE_PRIVATE)
        val totalDismissals = preferences.getInt("total_dismissals_allowed", 5)
        val intervalBetweenDismissals = preferences.getInt("interval_between_dismissals", 20)

        inputTotalDismissals.text = totalDismissals.toString()
        inputIntervalBetweenDismissals.text = intervalBetweenDismissals.toString()
    }

    private fun saveSettings() {
        val totalDismissals = inputTotalDismissals.text.toString().toIntOrNull() ?: 5
        val intervalBetweenDismissals =
            inputIntervalBetweenDismissals.text.toString().toIntOrNull() ?: 20

        val preferences = getSharedPreferences("dismiss_prefs", Context.MODE_PRIVATE)
        with(preferences.edit()) {
            putInt("total_dismissals_allowed", totalDismissals)
            putInt("interval_between_dismissals", intervalBetweenDismissals)
            putInt("dismiss_count", 0) // TODO: I'm not sure that i need to reset this value
            apply()
        }
    }

    private fun displayBootInfo() {
        lifecycleScope.launch {
            viewModel.allBootInfo.collect { bootInfoList ->
                val text = generateBootInfoText(bootInfoList)
                textBootInfo.text = text
            }
        }
    }

    private fun generateBootInfoText(bootInfoList: List<BootInfo>): String {
        if (bootInfoList.isEmpty()) {
            return "No boots detected"
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val bootInfoMap = bootInfoList.groupBy { dateFormat.format(it.date) }

        return bootInfoMap.entries.joinToString(separator = "\n") { (date, boots) ->
            "Date: $date, Boot events: ${boots.size}"
        }
    }

    private fun scheduleBootNotificationWorker() {
        val oneTimeWorkRequest = OneTimeWorkRequest.from(BootNotificationWorker::class.java)
        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)
    }
}