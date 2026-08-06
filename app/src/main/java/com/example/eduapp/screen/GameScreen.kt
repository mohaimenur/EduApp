package com.example.eduapp.screen

import android.content.Context
import android.content.res.Configuration
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eduapp.R
import com.example.eduapp.helper.rememberAssetImage
import com.example.eduapp.viewmodel.GameViewModel

/**
 * Main game execution screen.
 * Displays randomized puzzles, handles user input validation, and plays sound effects.
 */
@Composable
fun GameScreen(
    currentContext: Context,
    navController: NavHostController,
    gameViewModel: GameViewModel,
    username: String,
    level: String,
    modifier: Modifier = Modifier
) {
    // Starts the game session with provided username and level
    LaunchedEffect(username, level) {
        gameViewModel.startGame(username, level)
    }

    // Handles navigation to Score screen upon session completion
    LaunchedEffect(gameViewModel.gameFinished) {
        if (gameViewModel.gameFinished) {
            navController.navigate("score") {
                popUpTo("landing")
            }
        }
    }

    // Multimedia feedback for correct/incorrect answers
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

    // Retrieves the current puzzle asset
    val puzzle = gameViewModel.currentPuzzle
    val imageBitmap = puzzle?.let { rememberAssetImage(it.imagePath) }
    
    // UI Adaptation for Landscape vs Portrait
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val scrollState = rememberScrollState()

    // Answer validation logic
    val answerInput = gameViewModel.answerInput
    val isAnswerEmpty = answerInput.isBlank()
    val isAnswerTooLong = answerInput.length > 6
    val isAnswerInvalidFormat = answerInput.any { !it.isDigit() }
    val isAnswerInvalid = isAnswerEmpty || isAnswerTooLong || isAnswerInvalidFormat

    // Localized feedback messages for input errors
    val answerErrorMessage = when {
        isAnswerTooLong -> stringResource(R.string.error_answer_too_long)
        isAnswerInvalidFormat -> stringResource(R.string.error_answer_invalid)
        else -> null
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLandscape) {
            // Horizontal layout for wide screens
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Game Status Header
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.game_score, gameViewModel.score, gameViewModel.maxScore))
                        Text(stringResource(R.string.game_puzzle_index, gameViewModel.currentIndex + 1, gameViewModel.puzzles.size))
                        Text(stringResource(R.string.game_duration, gameViewModel.elapsedSeconds))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // User input with validation integration
                    OutlinedTextField(
                        value = answerInput,
                        onValueChange = { if (it.length <= 8) gameViewModel.answerInput = it },
                        label = { Text(stringResource(R.string.game_enter_answer)) },
                        singleLine = true,
                        isError = answerErrorMessage != null,
                        supportingText = { answerErrorMessage?.let { Text(it) } },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = { gameViewModel.checkAnswer() },
                        enabled = !isAnswerInvalid,
                        modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
                    ) { Text(stringResource(R.string.game_check)) }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Footer Info
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
            // Vertical layout for tall screens
            Spacer(Modifier.height(20.dp))

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
                value = answerInput,
                onValueChange = { if (it.length <= 8) gameViewModel.answerInput = it },
                label = { Text(stringResource(R.string.game_enter_answer)) },
                singleLine = true,
                isError = answerErrorMessage != null,
                supportingText = { answerErrorMessage?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { gameViewModel.checkAnswer() },
                enabled = !isAnswerInvalid,
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

            Spacer(Modifier.height(40.dp))
        }
    }

    // Modal dialog for answer result feedback
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
