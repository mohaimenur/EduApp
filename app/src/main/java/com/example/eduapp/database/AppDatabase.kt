package com.example.eduapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Main database definition for the application.
 * Manages the SQLite database for user scores using Room.
 */
@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    
    /**
     * Provides access to user-related database operations.
     */
    abstract fun appDao(): AppDao
}
