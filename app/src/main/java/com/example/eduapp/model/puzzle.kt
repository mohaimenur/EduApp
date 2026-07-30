

package com.example.eduapp.model

// A single puzzle: which image to show, and the number that counts as the
// correct answer when the player submits theirs on the Game screen.
// imagePath is relative to app/src/main/assets/ (e.g. "1/level01_pic01_0.png"),
// which is what AssetManager.open() expects in helper/utilis.kt.
data class Puzzle(
    val imagePath: String,
    val answer: Int
)

// Central answer key for every puzzle image, grouped by level (1 = easy, 3 = hard).
// GameViewModel reads from here - it never hardcodes image paths or answers itself.
object PuzzleBank {

    // NOTE: the 4 images per level ending in "_1" were generated for this project
    // and their answers are verified correct (I designed the equations myself).
    // The original images (pic01-pic06 in each level, supplied by you) are marked
    // TODO where I never solved the puzzle - those currently default to 0, which
    // will make the app mark them wrong no matter what the player enters. Open each
    // TODO image, work out the real answer, and replace the 0 before using it.

    val level1 = listOf(
        Puzzle("1/level01_pic01_0.png", 0),      // bunny/carrot puzzle - verified (carrot = 0)
        Puzzle("1/level01_pic02_21.png", 0),     // TODO: fill in real answer
        Puzzle("1/level01_pic03_15.png", 20),    // number pattern grid (x2 each step) - verified
        Puzzle("1/level01_pic04_55.jpg", 55),    // fidget spinners - verified
        Puzzle("1/level01_pic05_6.jpg", 0),      // TODO: fill in real answer
        Puzzle("1/level01_pic06_0.png", 0),      // TODO: double check (cat/cheese trick puzzle)
        Puzzle("1/level01_pic07_1.png", 0),      // apple/owl puzzle - verified (apple = 0)
        Puzzle("1/level01_pic08_1.png", 36),     // pattern grid (tripling: 4,12,36) - verified
        Puzzle("1/level01_pic09_1.png", 12),     // balloon puzzle - verified
        Puzzle("1/level01_pic10_1.png", 14)      // sun/moon puzzle - verified
    )

    val level2 = listOf(
        Puzzle("2/level02_pic01_31.jpg", 31),    // cat/pig/horse - verified
        Puzzle("2/level02_pic02_26.jpg", 0),     // TODO: fill in real answer
        Puzzle("2/level02_pic03_4.jpg", 0),      // TODO: fill in real answer
        Puzzle("2/level02_pic04_2.jpg", 0),      // TODO: fill in real answer
        Puzzle("2/level02_pic05_35.jpg", 0),     // TODO: fill in real answer
        Puzzle("2/level02_pic06_63.jpg", 63),    // cake/rice/taco/cheese - verified
        Puzzle("2/level02_pic07_1.png", 17),     // fruit chain (apple/banana/grape) - verified
        Puzzle("2/level02_pic08_1.png", 27),     // vehicles (equality + multiplication) - verified
        Puzzle("2/level02_pic09_1.png", 21),     // sports balls chain - verified
        Puzzle("2/level02_pic10_1.png", 18)      // desserts, "sum trick" (3 pairwise sums) - verified
    )

    val level3 = listOf(
        Puzzle("3/level03_pic01_27.jpg", 27),    // cat/rabbit/dog weight, "sum trick" - verified
        Puzzle("3/level03_pic02_4.jpg", 0),      // TODO: fill in real answer
        Puzzle("3/level03_pic03_5.jpg", 0),      // TODO: fill in real answer
        Puzzle("3/level03_pic04_24.jpg", 0),     // TODO: fill in real answer
        Puzzle("3/level03_pic05_25.jpg", 25),    // vehicles multi-op (x, /, equality) - verified
        Puzzle("3/level03_pic06_4.jpg", 0),      // TODO: fill in real answer
        Puzzle("3/level03_pic07_1.png", 27),     // weather, "sum trick" - verified
        Puzzle("3/level03_pic08_1.png", 21),     // vehicles multi-op (x, /) - verified
        Puzzle("3/level03_pic09_1.png", 19),     // space multi-op (x, -) - verified
        Puzzle("3/level03_pic10_1.png", 17)      // music multi-op (x, /) - verified
    )

    // Returns the full 10-puzzle list for a level ("1", "2", or "3").
    // Falls back to level1 if an unexpected value ever gets passed in.
    fun forLevel(level: String): List<Puzzle> = when (level) {
        "1" -> level1
        "2" -> level2
        "3" -> level3
        else -> level1
    }

    // Picks 3 random, distinct puzzles for one game session - this matches the
    // reference app's "Puzzle: X (/3)" behaviour, where each round only uses 3
    // of the level's images rather than all 10.
    fun sessionFor(level: String): List<Puzzle> = forLevel(level).shuffled().take(3)
}