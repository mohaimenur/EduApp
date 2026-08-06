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

// Scoring constants matching reference requirements
private const val POINTS_PER_PUZZLE = 10
private const val PUZZLES_PER_SESSION = 3

/**
 * ViewModel responsible for managing a single game session.
 * Encapsulates puzzle selection, scoring logic, timer management, and session persistence.
 */
class GameViewModel(private val dao: AppDao) : ViewModel() {

    // Identity and difficulty parameters for the current session
    var username by mutableStateOf("")
        private set
    var level by mutableStateOf("1")
        private set

    // Game state observables
    var puzzles by mutableStateOf<List<Puzzle>>(emptyList())
        private set
    var currentIndex by mutableIntStateOf(0)
        private set
    var score by mutableIntStateOf(0)
        private set
    var elapsedSeconds by mutableIntStateOf(0)
        private set

    // UI-bound input and interaction states
    var answerInput by mutableStateOf("")
    var dialogResult by mutableStateOf<Pair<Boolean, Int>?>(null)
        private set
    var isLastAnswerCorrect by mutableStateOf(false)
        private set

    // Lifecycle flag for navigation control
    var gameFinished by mutableStateOf(false)
        private set

    // Coroutine handle for the active timer
    private var timerJob: Job? = null

    /**
     * Computed property for the current active puzzle object.
     */
    val currentPuzzle: Puzzle?
        get() = puzzles.getOrNull(currentIndex)

    /**
     * Maximum possible score for the session.
     */
    val maxScore = PUZZLES_PER_SESSION * POINTS_PER_PUZZLE

    /**
     * Initializes a new game session.
     * Includes logic to prevent accidental resets during configuration changes (rotation).
     */
    fun startGame(username: String, level: String) {
        // Validation to prevent duplicate session initialization on rotation
        if (puzzles.isNotEmpty() && !gameFinished && this.username == username && this.level == level) {
            return
        }
        this.username = username
        this.level = level
        puzzles = PuzzleBank.sessionFor(level) // Picks 3 random puzzles
        currentIndex = 0
        score = 0
        elapsedSeconds = 0
        answerInput = ""
        dialogResult = null
        isLastAnswerCorrect = false
        gameFinished = false
        startTimer()
    }

    /**
     * Launches a background coroutine to increment the session timer every second.
     */
    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                elapsedSeconds++
            }
        }
    }

    /**
     * Safely halts the background timer.
     */
    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    /**
     * Logic to evaluate the user's submitted answer.
     * Updates the score and triggers the result dialog UI.
     */
    fun checkAnswer() {
        val puzzle = currentPuzzle ?: return
        val userAnswer = answerInput.trim().toIntOrNull()
        val correct = userAnswer != null && userAnswer == puzzle.answer
        isLastAnswerCorrect = correct
        
        if (correct) {
            score += POINTS_PER_PUZZLE
        }
        // Exposes result and correct answer for dynamic feedback display
        dialogResult = correct to puzzle.answer
    }

    /**
     * Advances the game state to the next puzzle or concludes the session.
     * Triggered by user dismissal of the result dialog.
     */
    fun dismissDialogAndAdvance() {
        dialogResult = null
        answerInput = ""
        if (currentIndex < puzzles.size - 1) {
            currentIndex++
        } else {
            finishGame()
        }
    }

    /**
     * Finalizes the session by stopping the timer and persisting results to the Room DB.
     */
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

    /**
     * Cleanup hook to ensure background jobs are stopped when the screen is exited.
     */
    override fun onCleared() {
        stopTimer()
    }
}
