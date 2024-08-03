package com.example.testtaskaura.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.testtaskaura.core.BootInfo
import com.example.testtaskaura.core.BootRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Locale

class MainViewModel(repository: BootRepository) : ViewModel() {

    private val allBootInfo: Flow<List<BootInfo>> = repository.allBootInfo
    val bootInfoText: Flow<String> = allBootInfo.map { generateBootInfoText(it) }

    private val _toastMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val toastMessage: Flow<String?> = _toastMessage


    fun saveSettings(context: Context) {

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
}