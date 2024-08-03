package com.example.testtaskaura

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: BootRepository) : ViewModel() {

    val allBootInfo: Flow<List<BootInfo>> = repository.allBootInfo

    fun insert(bootInfo: BootInfo) = viewModelScope.launch {
        repository.insert(bootInfo)
    }
}