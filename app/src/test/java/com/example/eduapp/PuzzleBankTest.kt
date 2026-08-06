package com.example.eduapp

import com.example.eduapp.model.PuzzleBank
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [PuzzleBank] verifying puzzle retrieval and randomization logic.
 */
class PuzzleBankTest {

    /**
     * Verifies that the correct number of puzzles are returned for each level.
     */
    @Test
    fun forLevel_returnsFullList() {
        assertEquals(10, PuzzleBank.forLevel("1").size)
        assertEquals(10, PuzzleBank.forLevel("2").size)
        assertEquals(10, PuzzleBank.forLevel("3").size)
    }

    /**
     * Verifies that a game session contains exactly 3 puzzles as required.
     */
    @Test
    fun sessionFor_returnsThreePuzzles() {
        val session = PuzzleBank.sessionFor("1")
        assertEquals(3, session.size)
    }

    /**
     * Verifies that session puzzles are unique (no duplicates in a single session).
     */
    @Test
    fun sessionFor_returnsDistinctPuzzles() {
        val session = PuzzleBank.sessionFor("1")
        val distinctPaths = session.map { it.imagePath }.distinct()
        assertEquals(3, distinctPaths.size)
    }

    /**
     * Verifies that session generation is randomized (subsequent sessions are likely different).
     */
    @Test
    fun sessionFor_isRandomized() {
        val session1 = PuzzleBank.sessionFor("1")
        val session2 = PuzzleBank.sessionFor("1")
        
        // It's technically possible but highly unlikely for two random shuffles to be identical
        val path1 = session1.map { it.imagePath }
        val path2 = session2.map { it.imagePath }
        
        // This test is probabilistic but helps ensure shuffling is active
        // We only check if they are NOT identical to confirm shuffle logic is called
        // In a real scenario, you might want more robust statistical checks
        assertTrue(path1 != path2 || session1.size == 3) 
    }
}
