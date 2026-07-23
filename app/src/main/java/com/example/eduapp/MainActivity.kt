@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.eduapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.eduapp.database.AppDatabase
import com.example.eduapp.screen.GameScreen
import com.example.eduapp.screen.LandingScreen
import com.example.eduapp.screen.ScoreScreen
import com.example.eduapp.screen.SettingScreen
import com.example.eduapp.screen.TestDBScreen
import com.example.eduapp.ui.theme.EduAppTheme
import com.example.eduapp.viewmodel.AppViewModel
import com.example.eduapp.viewmodel.AppViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val currentContext = applicationContext
        setContent {
            EduAppTheme {
                AppNav(currentContext)
            }

        }
    }
}

@Composable
fun AppNav (currentContext: Context) {
    val navController = rememberNavController ()
    NavHost ( navController = navController, startDestination = "landing" ) { //testDB
        composable ( "landing" ) {
            LandingScreen (navController)
        }
        composable ( "setting" ) {
            SettingScreen (navController)
        }
        composable ( "game" ) {
            GameScreen (currentContext, navController)
        }
        composable ( "score" ) {
            ScoreScreen (navController)
        }
        composable(route = "testDB"){TestDBScreen(currentContext)}
    }
}



//setting screen
@Composable

fun SettingScreen(navController: NavHostController, modifier: Modifier = Modifier) {

    Scaffold(

        topBar = { TopAppBar(title = { Text("Setting Screen") }) }

    ) {

            innerPadding ->

        Column(modifier

            .fillMaxSize()

            .padding(innerPadding)

            .padding(16.dp)) {

            Button(onClick = {navController.navigate("game")})

            { Text("Play Game") }

        }

    }

}











@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EduAppTheme {

    }
}