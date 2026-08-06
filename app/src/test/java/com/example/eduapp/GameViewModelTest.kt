package com.example.eduapp

import com.example.eduapp.database.AppDao
import com.example.eduapp.viewmodel.GameViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [GameViewModel] focusing on core game logic.
 * These tests run locally on the JVM without an Android device.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {

    private lateinit var viewModel: GameViewModel
    private val mockDao: AppDao = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // Sets the Main dispatcher to a test dispatcher for ViewModel scope execution
        Dispatchers.setMain(testDispatcher)
        viewModel = GameViewModel(mockDao)
    }

    @After
    fun tearDown() {
        // Resets the Main dispatcher to the original one after tests
        Dispatchers.resetMain()
    }

    /**
     * Verifies that starting a game correctly initializes the session state.
     */
    @Test
    fun startGame_initializesCorrectly() {
        viewModel.startGame("testUser", "1")
        
        assertEquals("testUser", viewModel.username)
        assertEquals("1", viewModel.level)
        assertEquals(0, viewModel.score)
        assertEquals(0, viewModel.currentIndex)
        assertFalse(viewModel.gameFinished)
        assertTrue(viewModel.puzzles.isNotEmpty())
    }

    /**
     * Verifies that entering a correct answer updates the score and triggers success feedback.
     */
    @Test
    fun checkAnswer_correctAnswer_updatesScore() {
        viewModel.startGame("testUser", "1")
        val currentPuzzle = viewModel.currentPuzzle!!
        
        // Simulates user typing the correct answer
        viewModel.answerInput = currentPuzzle.answer.toString()
        viewModel.checkAnswer()
        
        assertEquals(10, viewModel.score) // Assuming 10 points per puzzle
        assertTrue(viewModel.isLastAnswerCorrect)
        assertNotNull(viewModel.dialogResult)
        assertTrue(viewModel.dialogResult!!.first) // Result should be true
    }

    /**
     * Verifies that entering a wrong answer does not increment the score and triggers error feedback.
     */
    @Test
    fun checkAnswer_wrongAnswer_doesNotUpdateScore() {
        viewModel.startGame("testUser", "1")
        val currentPuzzle = viewModel.currentPuzzle!!
        
        // Simulates user typing an incorrect answer
        viewModel.answerInput = (currentPuzzle.answer + 1).toString()
        viewModel.checkAnswer()
        
        assertEquals(0, viewModel.score)
        assertFalse(viewModel.isLastAnswerCorrect)
        assertNotNull(viewModel.dialogResult)
        assertFalse(viewModel.dialogResult!!.first) // Result should be false
    }

    /**
     * Verifies that dismissing a dialog advances the puzzle index and resets input.
     */
    @Test
    fun dismissDialogAndAdvance_movesToNextPuzzle() {
        viewModel.startGame("testUser", "1")
        viewModel.answerInput = "123"
        viewModel.checkAnswer()
        
        viewModel.dismissDialogAndAdvance()
        
        assertEquals(1, viewModel.currentIndex)
        assertEquals("", viewModel.answerInput)
    }
}
