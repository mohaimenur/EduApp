@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.eduapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eduapp.ui.theme.EduAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduAppTheme {
                AppNav()
            }

        }
    }
}

@Composable
fun AppNav () {
    val navController = rememberNavController ()
    NavHost ( navController = navController, startDestination = "landing" ) {
        composable ( "landing" ) {
            LandingScreen (navController)
        }
        composable ( "setting" ) {
            SettingScreen (navController)
        }
        composable ( "game" ) {
            GameScreen (navController)
        }
        composable ( "score" ) {
            ScoreScreen (navController)
        }
    }
}

//landing screen
@Composable

fun LandingScreen(navController: NavHostController, modifier: Modifier = Modifier) {

    Scaffold(

        topBar = { TopAppBar(title = { Text("Landing Screen") }) }

    ) {

            innerPadding ->

        Column(modifier

            .fillMaxSize()

            .padding(innerPadding)

            .padding(16.dp)) {

            Button(onClick = {navController.navigate("setting")})

            { Text("Go to Setting") }

        }

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


//game screen
@Composable

fun GameScreen(navController: NavHostController, modifier: Modifier = Modifier) {

    Scaffold(

        topBar = { TopAppBar(title = { Text("Game Screen") }) }

    ) {

            innerPadding ->

        Column(modifier

            .fillMaxSize()

            .padding(innerPadding)

            .padding(16.dp)) {

            Button(onClick = {navController.navigate("score")})

            { Text("Display Score") }

        }

    }

}


//score screen
@Composable

fun ScoreScreen(navController: NavHostController, modifier: Modifier = Modifier) {

    Scaffold(

        topBar = { TopAppBar(title = { Text("Score Screen") }) }

    ) {

            innerPadding ->

        Column(modifier

            .fillMaxSize()

            .padding(innerPadding)

            .padding(16.dp)) {

            Button(onClick = {navController.navigate("landing")})

            { Text("Go Back to Landing") }

        }

    }

}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EduAppTheme {

    }
}