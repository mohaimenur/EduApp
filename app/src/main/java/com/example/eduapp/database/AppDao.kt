package com.example.eduapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for user score history operations.
 * Defines the SQL queries and internal mappings for Room.
 */
@Dao
interface AppDao {
    /**
     * Persists a new user record.
     */
    @Insert
    suspend fun insert(user: User)

    /**
     * Updates an existing user record based on its primary key.
     */
    @Update
    suspend fun update(user: User)

    /**
     * Removes a specific user record from the database.
     */
    @Delete
    suspend fun delete(user: User)

    /**
     * Streams the full list of user records, ordered by most recent first.
     */
    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAllUsers(): Flow<List<User>>

    /**
     * Securely deletes all records from the 'users' table.
     */
    @Query("DELETE FROM users")
    suspend fun deleteAll()
}
