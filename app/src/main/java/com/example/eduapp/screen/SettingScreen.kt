package com.example.eduapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavHostController,
    selectedLevel: String,
    onLevelChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val levels = listOf("1", "2", "3")
    
    Scaffold(
        topBar = { TopAppBar(title = { Text("EduApp - Settings") }) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(text = "Choose Difficulty Level:")
            
            Spacer(modifier = Modifier.height(16.dp))
            
            levels.forEach { level ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = (level == selectedLevel),
                        onClick = { onLevelChange(level) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Level $level")
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { navController.navigate("game") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Game")
            }
        }
    }
}
