@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.eduapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.eduapp.database.AppDatabase
import com.example.eduapp.screen.GameScreen
import com.example.eduapp.screen.LandingScreen
import com.example.eduapp.screen.ScoreScreen
import com.example.eduapp.screen.SettingScreen
import com.example.eduapp.ui.theme.EduAppTheme
import com.example.eduapp.viewmodel.AppViewModel
import com.example.eduapp.viewmodel.AppViewModelFactory
import com.example.eduapp.viewmodel.GameViewModel
import com.example.eduapp.viewmodel.GameViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // applicationContext (not the Activity itself) so the Room DB
        // instance below doesn't leak the Activity if it's ever held onto
        // longer than the Activity lives.
        val currentContext = applicationContext
        setContent {
            EduAppTheme {
                AppNav(currentContext)
            }
        }
    }
}

// Hosts the nav graph and everything screens need to share between routes:
// the Room database, the two ViewModels, and the username/level picked on
// earlier screens (plain remembered state, since only Landing/Setting write
// to it and everything else just reads it).
@Composable
fun AppNav(currentContext: Context) {
    val navController = rememberNavController()

    // Built once per app launch and shared by every screen that touches the
    // database (Game writes results, Score reads them).
    val db = remember {
        Room.databaseBuilder(currentContext, AppDatabase::class.java, "app_db").build()
    }
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory(db.appDao()))
    val gameViewModel: GameViewModel = viewModel(factory = GameViewModelFactory(db.appDao()))

    // Carried from Landing -> Setting -> Game. Kept here (not inside a
    // screen) because both Landing and Setting need to write to it, and Game
    // needs to read the final values after both have run.
    var username by remember { mutableStateOf("") }
    var selectedLevel by remember { mutableStateOf("1") }

    NavHost(navController = navController, startDestination = "landing") {
        composable("landing") {
            LandingScreen(
                navController = navController,
                username = username,
                onUsernameChange = { username = it }
            )
        }
        composable("setting") {
            SettingScreen(
                navController = navController,
                selectedLevel = selectedLevel,
                onLevelChange = { selectedLevel = it }
            )
        }
        composable("game") {
            GameScreen(
                currentContext = currentContext,
                navController = navController,
                gameViewModel = gameViewModel,
                username = username,
                level = selectedLevel
            )
        }
        composable("score") {
            ScoreScreen(navController = navController, appViewModel = appViewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EduAppTheme {
    }
}