package com.example.eduapp.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eduapp.R

/**
 * The initial screen of the application.
 * Captures the user's name and provides an entry point to the game settings.
 */
@Composable
fun LandingScreen(
    navController: NavHostController,
    username: String,
    onUsernameChange: (String) -> Unit,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Controls the visibility of the language selection dropdown
    var langExpanded by rememberSaveable { mutableStateOf(false) }
    
    // UI adaptation based on screen orientation
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val scrollState = rememberScrollState()

    // Real-time Input Validation logic
    val isUserNameEmpty = username.isBlank()
    val isUserNameTooLong = username.length > 15
    val hasInvalidChars = username.any { !it.isLetterOrDigit() && it != ' ' }
    
    // Aggregated state to enable/disable navigation
    val isUsernameInvalid = isUserNameEmpty || isUserNameTooLong || hasInvalidChars

    // Localized error message calculation
    val errorMessage = when {
        isUserNameTooLong -> stringResource(R.string.error_username_too_long)
        hasInvalidChars -> stringResource(R.string.error_username_invalid)
        isUserNameEmpty && username.isNotEmpty() -> stringResource(R.string.error_username_empty)
        else -> null
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (isLandscape) {
            // Landscape optimized layout: Logo on left, input on right
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 48.dp, end = 64.dp, top = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        text = stringResource(R.string.app_name),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Column(
                    modifier = Modifier.weight(1.2f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { if (it.length <= 20) onUsernameChange(it) },
                        label = { Text(stringResource(R.string.enter_username)) },
                        singleLine = true,
                        isError = errorMessage != null,
                        supportingText = { errorMessage?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = { navController.navigate("setting") },
                        enabled = !isUsernameInvalid,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) { Text(stringResource(R.string.play)) }
                }
            }
        } else {
            // Portrait standard layout: Stacked vertically
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(60.dp))

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

                OutlinedTextField(
                    value = username,
                    onValueChange = { if (it.length <= 20) onUsernameChange(it) },
                    label = { Text(stringResource(R.string.enter_username)) },
                    singleLine = true,
                    isError = errorMessage != null,
                    supportingText = { errorMessage?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { navController.navigate("setting") },
                    enabled = !isUsernameInvalid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) { Text(stringResource(R.string.play)) }

                Spacer(Modifier.height(40.dp))
            }
        }

        // Floating Language Switcher in the top-right corner
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
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
    }
}
