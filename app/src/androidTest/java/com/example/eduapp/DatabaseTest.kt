package com.example.eduapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.eduapp.database.AppDao
import com.example.eduapp.database.AppDatabase
import com.example.eduapp.database.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Instrumented tests for the Room database [AppDatabase].
 * These tests run on a real Android device or emulator to verify persistence logic.
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var appDao: AppDao
    private lateinit var db: AppDatabase

    /**
     * Initializes an in-memory database before each test.
     * Data is cleared when the test finishes.
     */
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        appDao = db.appDao()
    }

    /**
     * Closes the database after each test execution.
     */
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /**
     * Verifies that a user record can be successfully inserted and retrieved.
     */
    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() = runBlocking {
        val user = User(username = "testUser", level = "2", score = 20, duration = 120)
        appDao.insert(user)
        val allUsers = appDao.getAllUsers().first()
        assertEquals(allUsers[0].username, "testUser")
        assertEquals(allUsers[0].level, "2")
        assertEquals(allUsers[0].score, 20)
        assertEquals(allUsers[0].duration, 120)
    }

    /**
     * Verifies that the deleteAll function clears the entire history table.
     */
    @Test
    @Throws(Exception::class)
    fun deleteAllUsers() = runBlocking {
        val user1 = User(username = "user1")
        val user2 = User(username = "user2")
        appDao.insert(user1)
        appDao.insert(user2)
        
        appDao.deleteAll()
        val allUsers = appDao.getAllUsers().first()
        assertEquals(allUsers.size, 0)
    }

    /**
     * Verifies that user records are returned in descending order (newest first).
     */
    @Test
    @Throws(Exception::class)
    fun getAllUsersOrderedByDesc() = runBlocking {
        val user1 = User(username = "user1")
        val user2 = User(username = "user2")
        appDao.insert(user1)
        appDao.insert(user2)

        val allUsers = appDao.getAllUsers().first()
        assertEquals(allUsers.size, 2)
        assertEquals(allUsers[0].username, "user2") // newest record should be first
        assertEquals(allUsers[1].username, "user1")
    }
}
