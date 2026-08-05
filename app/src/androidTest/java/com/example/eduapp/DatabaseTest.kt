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

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var appDao: AppDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        appDao = db.appDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

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

    @Test
    @Throws(Exception::class)
    fun getAllUsersOrderedByDesc() = runBlocking {
        val user1 = User(username = "user1")
        val user2 = User(username = "user2")
        appDao.insert(user1)
        appDao.insert(user2)

        val allUsers = appDao.getAllUsers().first()
        assertEquals(allUsers.size, 2)
        assertEquals(allUsers[0].username, "user2")
        assertEquals(allUsers[1].username, "user1")
    }
}
