@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.eduapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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

    val title = when (currentRoute) {
        "landing" -> ""
        "setting" -> "SETTING"
        "game" -> "Game Screen"
        "score" -> "SCORE LIST"
        else -> ""
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(title) }) },
        bottomBar = {
            if (currentRoute != "game") {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == "landing",
                        onClick = { navController.navigate("landing") {
                            popUpTo("landing") { inclusive = true }
                        } },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                    )
                    NavigationBarItem(
                        selected = currentRoute == "setting",
                        onClick = { navController.navigate("setting") },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                        label = { Text("Settings") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "score",
                        onClick = { navController.navigate("score") },
                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Score") },
                        label = { Text("Score") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "landing",
            modifier = Modifier.padding(innerPadding)
        ) {
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
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EduAppTheme {
    }
}