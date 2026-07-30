package com.example.eduapp.screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eduapp.helper.rememberAssetImage
import com.example.eduapp.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    currentContext: Context,
    navController: NavHostController,
    gameViewModel: GameViewModel,
    username: String,
    level: String,
    modifier: Modifier = Modifier
) {
    // Start game when screen is first shown
    LaunchedEffect(key1 = true) {
        gameViewModel.startGame(username, level)
    }

    // Navigate to score screen when game is finished
    if (gameViewModel.gameFinished) {
        LaunchedEffect(key1 = true) {
            navController.navigate("score") {
                popUpTo("landing")
            }
        }
    }

    val puzzle = gameViewModel.currentPuzzle
    val imageBitmap = puzzle?.let { rememberAssetImage(it.imagePath) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Level $level - Round ${gameViewModel.currentIndex + 1}/3") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Score: ${gameViewModel.score}")
                Text(text = "Time: ${gameViewModel.elapsedSeconds}s")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Puzzle Image",
                    modifier = Modifier
                        .size(300.dp)
                        .weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "Loading puzzle...")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = gameViewModel.answerInput,
                onValueChange = { gameViewModel.answerInput = it },
                label = { Text("Your Answer") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { gameViewModel.checkAnswer() },
                enabled = gameViewModel.answerInput.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("CHECK")
            }
        }
    }

    // Show feedback dialog
    gameViewModel.dialogMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { /* Don't dismiss by clicking outside */ },
            title = { Text(text = "Result") },
            text = { Text(text = message) },
            confirmButton = {
                Button(onClick = { gameViewModel.dismissDialogAndAdvance() }) {
                    Text("OK")
                }
            }
        )
    }
}
