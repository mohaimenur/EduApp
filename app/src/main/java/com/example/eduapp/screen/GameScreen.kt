package com.example.eduapp.screen

import android.content.Context
import android.content.res.Configuration
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import java.util.Locale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eduapp.R
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

    // Play sound effects when a result dialog appears
    val context = LocalContext.current
    LaunchedEffect(gameViewModel.dialogResult) {
        gameViewModel.dialogResult?.let {
            val resId = if (gameViewModel.isLastAnswerCorrect) {
                R.raw.correct_answer_beep
            } else {
                R.raw.wrong_answer_beep
            }
            
            MediaPlayer.create(context, resId)?.apply {
                setOnCompletionListener { release() }
                start()
            }
        }
    }

    val puzzle = gameViewModel.currentPuzzle
    // rememberAssetImage (from helper/utilis.kt) loads the PNG/JPG out of
    // app/src/main/assets/ using currentPuzzle.imagePath.
    val imageBitmap = puzzle?.let { rememberAssetImage(it.imagePath) }
    
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLandscape) {
            // Landscape Layout: Split into two columns
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Left Column: Image
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = stringResource(R.string.app_name),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.game_error_image),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                // Right Column: Controls
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Status row
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.game_score, gameViewModel.score, gameViewModel.maxScore))
                        Text(stringResource(R.string.game_puzzle_index, gameViewModel.currentIndex + 1, gameViewModel.puzzles.size))
                        Text(stringResource(R.string.game_duration, gameViewModel.elapsedSeconds))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = gameViewModel.answerInput,
                        onValueChange = { gameViewModel.answerInput = it },
                        label = { Text(stringResource(R.string.game_enter_answer)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = { gameViewModel.checkAnswer() },
                        enabled = gameViewModel.answerInput.isNotBlank(),
                        modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
                    ) { Text(stringResource(R.string.game_check)) }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(stringResource(R.string.game_user, username))
                        Text(stringResource(R.string.game_level, level))
                    }
                }
            }
        } else {
            // Portrait Layout: Original sequential layout
            Spacer(Modifier.weight(0.5f))

            // Status row: Score / Puzzle X of 3 / elapsed time.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.game_score, gameViewModel.score, gameViewModel.maxScore))
                Text(stringResource(R.string.game_puzzle_index, gameViewModel.currentIndex + 1, gameViewModel.puzzles.size))
                Text(stringResource(R.string.game_duration, gameViewModel.elapsedSeconds))
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            } else {
                Text(
                    text = stringResource(R.string.game_error_image),
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = gameViewModel.answerInput,
                onValueChange = { gameViewModel.answerInput = it },
                label = { Text(stringResource(R.string.game_enter_answer)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { gameViewModel.checkAnswer() },
                enabled = gameViewModel.answerInput.isNotBlank(),
                modifier = Modifier.padding(top = 8.dp)
            ) { Text(stringResource(R.string.game_check)) }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.game_user, username))
                Text(stringResource(R.string.game_level, level))
            }

            Spacer(Modifier.weight(1f))
        }
    }

    // Result popup shown after CHECK, mirroring the reference app's
    // custom_dialog + Ok button. onDismissRequest is intentionally a no-op
    // so tapping outside the dialog can't skip a puzzle without answering -
    // the only way out is the Ok button, which calls dismissDialogAndAdvance().
    gameViewModel.dialogResult?.let { (isCorrect, answer) ->
        val message = if (isCorrect) {
            stringResource(R.string.game_correct)
        } else {
            stringResource(R.string.game_wrong, answer)
        }
        AlertDialog(
            onDismissRequest = { },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = { gameViewModel.dismissDialogAndAdvance() }) {
                    Text(stringResource(R.string.game_ok))
                }
            }
        )
    }
}
