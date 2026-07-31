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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eduapp.helper.rememberAssetImage
import com.example.eduapp.viewmodel.GameViewModel

// The main play screen: shows the current puzzle image, takes a numeric
// answer, and drives the round via GameViewModel. This screen has no game
// logic of its own - it only reads GameViewModel's state and calls its
// functions in response to taps.
@Composable
fun GameScreen(
    currentContext: Context,
    navController: NavHostController,
    gameViewModel: GameViewModel,
    username: String,
    level: String,
    modifier: Modifier = Modifier
) {
    // Runs once when this screen first appears (or if username/level somehow
    // change), kicking off a fresh 3-puzzle round.
    LaunchedEffect(username, level) {
        gameViewModel.startGame(username, level)
    }

    // Watches gameFinished and navigates to Score the moment the 3rd puzzle
    // is answered. popUpTo("landing") clears Setting/Game off the back stack
    // so the back button from Score goes to Landing, not back into the
    // finished game.
    LaunchedEffect(gameViewModel.gameFinished) {
        if (gameViewModel.gameFinished) {
            navController.navigate("score") {
                popUpTo("landing")
            }
        }
    }

    val puzzle = gameViewModel.currentPuzzle
    // rememberAssetImage (from helper/utilis.kt) loads the PNG/JPG out of
    // app/src/main/assets/ using currentPuzzle.imagePath.
    val imageBitmap = puzzle?.let { rememberAssetImage(it.imagePath) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Status row: Score / Puzzle X of 3 / elapsed time.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Score: ${gameViewModel.score} (/${gameViewModel.maxScore})")
            Text("Puzzle: ${gameViewModel.currentIndex + 1} (/${gameViewModel.puzzles.size})")
            Text("Duration: ${gameViewModel.elapsedSeconds}s")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Falls back to an error message instead of crashing if the
        // asset failed to decode (e.g. a bad file path).
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap,
                contentDescription = "Puzzle image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        } else {
            Text(
                text = "Error: Could not load puzzle image",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Numeric keyboard only, since every puzzle answer is an integer.
        OutlinedTextField(
            value = gameViewModel.answerInput,
            onValueChange = { gameViewModel.answerInput = it },
            label = { Text("Enter your answer ...") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Disabled until something is typed, so CHECK can't be tapped on
        // an empty answer.
        Button(
            onClick = { gameViewModel.checkAnswer() },
            enabled = gameViewModel.answerInput.isNotBlank(),
            modifier = Modifier.padding(top = 8.dp)
        ) { Text("CHECK") }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("User: $username")
            Text("Level: $level")
        }
    }

    // Result popup shown after CHECK, mirroring the reference app's
    // custom_dialog + Ok button. onDismissRequest is intentionally a no-op
    // so tapping outside the dialog can't skip a puzzle without answering -
    // the only way out is the Ok button, which calls dismissDialogAndAdvance().
    gameViewModel.dialogMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = { gameViewModel.dismissDialogAndAdvance() }) {
                    Text("Ok")
                }
            }
        )
    }
}
