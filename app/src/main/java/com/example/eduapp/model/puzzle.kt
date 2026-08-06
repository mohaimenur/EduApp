package com.example.eduapp.model

/**
 * Domain model representing a single mathematical or logic puzzle.
 * 
 * @property imagePath Relative path to the image asset (e.g., "1/level01_pic01_0.png").
 * @property answer The integer solution required to solve the puzzle.
 */
data class Puzzle(
    val imagePath: String,
    val answer: Int
)

/**
 * Data bank containing all puzzles available in the application.
 * Puzzles are categorized into three difficulty levels.
 */
object PuzzleBank {

    // Easy puzzles (Level 1)
    val level1 = listOf(
        Puzzle("1/level01_pic01_0.png", 0),
        Puzzle("1/level01_pic02_21.png", 0), // TODO: Verify real answer
        Puzzle("1/level01_pic03_15.png", 20),
        Puzzle("1/level01_pic04_55.jpg", 55),
        Puzzle("1/level01_pic05_6.jpg", 0),  // TODO: Verify real answer
        Puzzle("1/level01_pic06_0.png", 0),  // TODO: Verify real answer
        Puzzle("1/level01_pic07_1.png", 0),
        Puzzle("1/level01_pic08_1.png", 36),
        Puzzle("1/level01_pic09_1.png", 12),
        Puzzle("1/level01_pic10_1.png", 14)
    )

    // Medium puzzles (Level 2)
    val level2 = listOf(
        Puzzle("2/level02_pic01_31.jpg", 31),
        Puzzle("2/level02_pic02_26.jpg", 0), // TODO: Verify real answer
        Puzzle("2/level02_pic03_4.jpg", 0),  // TODO: Verify real answer
        Puzzle("2/level02_pic04_2.jpg", 0),  // TODO: Verify real answer
        Puzzle("2/level02_pic05_35.jpg", 0), // TODO: Verify real answer
        Puzzle("2/level02_pic06_63.jpg", 63),
        Puzzle("2/level02_pic07_1.png", 17),
        Puzzle("2/level02_pic08_1.png", 27),
        Puzzle("2/level02_pic09_1.png", 21),
        Puzzle("2/level02_pic10_1.png", 18)
    )

    // Hard puzzles (Level 3)
    val level3 = listOf(
        Puzzle("3/level03_pic01_27.jpg", 27),
        Puzzle("3/level03_pic02_4.jpg", 0),  // TODO: Verify real answer
        Puzzle("3/level03_pic03_5.jpg", 0),  // TODO: Verify real answer
        Puzzle("3/level03_pic04_24.jpg", 0), // TODO: Verify real answer
        Puzzle("3/level03_pic05_25.jpg", 25),
        Puzzle("3/level03_pic06_4.jpg", 0),  // TODO: Verify real answer
        Puzzle("3/level03_pic07_1.png", 27),
        Puzzle("3/level03_pic08_1.png", 21),
        Puzzle("3/level03_pic09_1.png", 19),
        Puzzle("3/level03_pic10_1.png", 17)
    )

    /**
     * Retrieves the list of all puzzles for a specific level.
     */
    fun forLevel(level: String): List<Puzzle> = when (level) {
        "1" -> level1
        "2" -> level2
        "3" -> level3
        else -> level1
    }

    /**
     * Shuffles and selects a subset of puzzles for a single game session.
     * Currently configured to select 3 puzzles per session.
     */
    fun sessionFor(level: String): List<Puzzle> = forLevel(level).shuffled().take(3)
}
