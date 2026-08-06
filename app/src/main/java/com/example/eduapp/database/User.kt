package com.example.eduapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data Entity representing a single user score entry in the 'users' table.
 * 
 * @property id Unique identifier for the record (auto-generated).
 * @property username The name entered by the player.
 * @property level The difficulty level played ("1", "2", or "3").
 * @property score The total points accumulated during the session.
 * @property duration The time taken in seconds to complete the session.
 * @property date The timestamp of when the score was recorded.
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val level: String = "1",
    val score: Int = 0,
    val duration: Int = 0,
    val date: Long = System.currentTimeMillis()
)
