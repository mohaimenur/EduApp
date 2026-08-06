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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.eduapp.database.AppDatabase
import com.example.eduapp.helper.LocaleHelper
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
        val currentContext = applicationContext
        setContent {
            EduAppTheme(dynamicColor = false) {
                AppNav(currentContext)
            }
        }
    }
}

@Composable
fun AppNav(applicationContext: Context) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val db = remember {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_db").build()
    }
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory(db.appDao()))
    val gameViewModel: GameViewModel = viewModel(factory = GameViewModelFactory(db.appDao()))

    var username by rememberSaveable { mutableStateOf("") }
    var selectedLevel by rememberSaveable { mutableStateOf("1") }
    var language by rememberSaveable { mutableStateOf("en") }

    val activityContext = LocalContext.current
    val localizedContext = remember(language) {
        LocaleHelper.setLocale(activityContext, language)
    }

    CompositionLocalProvider(LocalContext provides localizedContext) {
        androidx.compose.runtime.key(language) {
            val title = when (currentRoute) {
                "landing" -> ""
                "setting" -> stringResource(R.string.settings)
                "game" -> stringResource(R.string.game_screen_title)
                "score" -> stringResource(R.string.score_list)
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
                                label = { Text(stringResource(R.string.home)) },
                            )
                            NavigationBarItem(
                                selected = currentRoute == "setting",
                                onClick = { navController.navigate("setting") },
                                icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                                label = { Text(stringResource(R.string.settings)) }
                            )
                            NavigationBarItem(
                                selected = currentRoute == "score",
                                onClick = { navController.navigate("score") },
                                icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Score") },
                                label = { Text(stringResource(R.string.score)) }
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
                            onUsernameChange = { username = it },
                            currentLanguage = language,
                            onLanguageChange = { language = it }
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
                            currentContext = localizedContext,
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
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EduAppTheme {
    }
}
