package com.example.eduapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eduapp.R

// First screen the player sees. Just captures a username, then hands off to
// level selection. Mirrors the reference app's "Welcome To The Game" +
// username field + PLAY button.
@Composable
fun LandingScreen(
    navController: NavHostController,
    username: String,
    onUsernameChange: (String) -> Unit,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var langExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        // Language Button in Top-Right Corner
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            IconButton(onClick = { langExpanded = true }) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = stringResource(R.string.select_language),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            DropdownMenu(
                expanded = langExpanded,
                onDismissRequest = { langExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("English") },
                    onClick = {
                        onLanguageChange("en")
                        langExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("中文") },
                    onClick = {
                        onLanguageChange("zh")
                        langExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("বাংলা") },
                    onClick = {
                        onLanguageChange("bn")
                        langExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("हिन्दी") },
                    onClick = {
                        onLanguageChange("hi")
                        langExpanded = false
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f)) // Push content down a bit

            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = stringResource(R.string.app_name),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // username/onUsernameChange are "hoisted" - the actual value lives
            // in AppNav, this composable just displays it and reports changes
            // back up, so the value survives navigating to other screens.
            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text(stringResource(R.string.enter_username)) },
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
            ) { Text(stringResource(R.string.play)) }

            Spacer(Modifier.weight(2f)) // Larger spacer at bottom pushes content above middle
        }
    }
}
