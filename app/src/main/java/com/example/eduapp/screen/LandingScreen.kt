package com.example.eduapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

// First screen the player sees. Just captures a username, then hands off to
// level selection. Mirrors the reference app's "Welcome To The Game" +
// username field + PLAY button.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    navController: NavHostController,
    username: String,
    onUsernameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Welcome To The Game") }) }
    ) { innerPadding ->
        Column(
            modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // username/onUsernameChange are "hoisted" - the actual value lives
            // in AppNav, this composable just displays it and reports changes
            // back up, so the value survives navigating to other screens.
            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text("Enter a username ...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Disabled until something is typed, so you can't start a round
            // with a blank username.
            Button(
                onClick = { navController.navigate("setting") },
                enabled = username.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) { Text("PLAY") }
        }
    }
}