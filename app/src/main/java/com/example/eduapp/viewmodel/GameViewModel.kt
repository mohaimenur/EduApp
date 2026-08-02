package com.example.eduapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduapp.database.AppDao
import com.example.eduapp.database.User
import com.example.eduapp.model.Puzzle
import com.example.eduapp.model.PuzzleBank
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.getOrNull

// One correct answer = 10 points. 3 puzzles per round = 30 max, matching the
// reference app's "Score: 0 (/30)" label.
private const val POINTS_PER_PUZZLE = 10
private const val PUZZLES_PER_SESSION = 3

// Owns everything that happens during one play-through: which 3 puzzles were
// picked, which one is showing now, the running score/timer, and the text the
// player has typed. GameScreen just reads these as state and calls the
// functions below - it holds no game logic itself.
class GameViewModel(private val dao: AppDao) : ViewModel() {

    // "private set" means other classes (GameScreen) can read these but can't
    // reassign them directly - they can only change via the functions below.
    var username by mutableStateOf("")
        private set
    var level by mutableStateOf("1")
        private set

    var puzzles by mutableStateOf<List<Puzzle>>(emptyList())
        private set
    var currentIndex by mutableIntStateOf(0)
        private set
    var score by mutableIntStateOf(0)
        private set
    var elapsedSeconds by mutableIntStateOf(0)
        private set

    // These two ARE meant to be set directly from the UI:
    // answerInput is two-way bound to the TextField, dialogMessage is read to
    // decide whether the result AlertDialog is showing.
    var answerInput by mutableStateOf("")
    var dialogMessage by mutableStateOf<String?>(null)
        private set
    var isLastAnswerCorrect by mutableStateOf(false)
        private set

    // Flips to true once all 3 puzzles are done - GameScreen watches this and
    // navigates to the Score screen when it changes.
    var gameFinished by mutableStateOf(false)
        private set

    // Handle to the coroutine that ticks elapsedSeconds every second, so we
    // can cancel it when the round ends or the ViewModel is destroyed.
    private var timerJob: Job? = null

    // The puzzle currently on screen, or null if puzzles hasn't loaded yet /
    // the round is already finished.
    val currentPuzzle: Puzzle?
        get() = puzzles.getOrNull(currentIndex)

    val maxScore = PUZZLES_PER_SESSION * POINTS_PER_PUZZLE

    // Called once when GameScreen first appears (see GameScreen's
    // LaunchedEffect). Resets all state and picks a fresh set of 3 puzzles.
    fun startGame(username: String, level: String) {
        this.username = username
        this.level = level
        puzzles = PuzzleBank.sessionFor(level)
        currentIndex = 0
        score = 0
        elapsedSeconds = 0
        answerInput = ""
        dialogMessage = null
        isLastAnswerCorrect = false
        gameFinished = false
        startTimer()
    }

    // Simple 1-second-tick stopwatch. viewModelScope auto-cancels this if the
    // ViewModel is ever cleared, but we also cancel it explicitly in
    // finishGame() so it stops the moment the round ends.
    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                elapsedSeconds++
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    // Called when the player taps CHECK. Compares their typed answer to the
    // current puzzle's answer and sets dialogMessage, which makes the result
    // AlertDialog appear in GameScreen.
    fun checkAnswer() {
        val puzzle = currentPuzzle ?: return
        val userAnswer = answerInput.trim().toIntOrNull()
        val correct = userAnswer != null && userAnswer == puzzle.answer
        isLastAnswerCorrect = correct
        
        if (correct) {
            score += POINTS_PER_PUZZLE
            dialogMessage = "Correct! Your brain is on fire! ⚡"
        } else {
            dialogMessage = "Error 404: Brain not found! The answer was ${puzzle.answer}. 🤖"
        }
    }

    // Called when the player taps "Ok" on the result dialog. Moves to the
    // next puzzle, or ends the round if that was the last of the 3.
    fun dismissDialogAndAdvance() {
        dialogMessage = null
        answerInput = ""
        if (currentIndex < puzzles.size - 1) {
            currentIndex++
        } else {
            finishGame()
        }
    }

    // Stops the timer, marks the round finished, and saves the result to
    // Room so it shows up on the Score screen. Runs on viewModelScope since
    // dao.insert is a suspend function (database work off the main thread).
    private fun finishGame() {
        stopTimer()
        gameFinished = true
        viewModelScope.launch {
            dao.insert(
                User(
                    username = username.ifBlank { "Player" },
                    level = level,
                    score = score,
                    duration = elapsedSeconds
                )
            )
        }
    }

    // Safety net: if the screen/ViewModel is destroyed mid-game (e.g. user
    // backs out), make sure the timer coroutine doesn't keep running.
    override fun onCleared() {
        stopTimer()
    }
}