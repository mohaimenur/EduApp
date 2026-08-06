package com.example.eduapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduapp.database.AppDao
import com.example.eduapp.database.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Shared ViewModel for managing application-wide data, primarily focused on
 * user score history operations.
 */
class AppViewModel(private val dao: AppDao) : ViewModel() {

    /**
     * Exposes a real-time observable list of all users from the database.
     * Uses Flow to automatically trigger UI updates on data changes.
     */
    val users: Flow<List<User>> = dao.getAllUsers()

    /**
     * Adds a new user record to the database.
     */
    fun addUser(username: String) {
        viewModelScope.launch {
            val user = User(username = username)
            dao.insert(user)
        }
    }

    /**
     * Deletes a specific user record.
     */
    fun deleteUser(user: User) {
        viewModelScope.launch {
            dao.delete(user)
        }
    }

    /**
     * Updates an existing user record (e.g., username editing).
     */
    fun updateUser(user: User) {
        viewModelScope.launch {
            dao.update(user)
        }
    }

    /**
     * Wipes all user records from the history table.
     */
    fun clearUsers() {
        viewModelScope.launch {
            dao.deleteAll()
        }
    }
}
