package com.example.eduapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eduapp.database.AppDao

class AppViewModelFactory(private val dao: AppDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class GameViewModelFactory(private val dao: AppDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
