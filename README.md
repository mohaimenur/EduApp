# BrainDrain

A picture-puzzle math game built with Kotlin and Jetpack Compose. Pick a difficulty level, solve 3 picture-based math puzzles against the clock, and see how you rank on the score history. This is an offline game; no network required.

## Features

- **3 difficulty levels**, 10 hand-picked/generated picture puzzles each (30 total)
- **Timed rounds** — each session picks 3 random puzzles from the chosen level and tracks elapsed time
- **Score out of 30** — 10 points per correct answer, saved automatically when a round finishes
- **Persistent score history** — every past round (username, level, score, duration, date) is stored with Room and shown on the Score screen
- **Multi-language support** — English, Chinese, Hindi, and Bengali, switchable from the Landing screen
- **Bottom navigation** — Home / Settings / Score, shown on every screen except the active game so a stray tap can't interrupt a round

## Built With

- **Kotlin** — main programming language
- **Jetpack Compose** — modern declarative UI toolkit
- **Material Design 3** — UI components and theming
- **Navigation Compose** — screen-to-screen navigation
- **ViewModel + Compose State** — manages game/session state and survives recomposition
- **Room** — local database for the score history

## Puzzle Content

Every puzzle is a picture with a math equation to solve, shown as a PNG/JPG bundled in the app (`app/src/main/assets/1`, `/2`, `/3` — one folder per level). The correct answer for each image is looked up from a hardcoded answer key rather than an API, since the puzzles are fixed content rather than live data.

## App Structure


app/

├── **model/** → `puzzle.kt` (Puzzle data class + `PuzzleBank` answer key)  
├── **viewmodel/** → `GameViewModel` (active round state), `AppViewModel` (score history)  
├── **database/** → `AppDatabase`, `AppDao`, `User` (Room)  
├── **screen/** → `LandingScreen`, `SettingScreen`, `GameScreen`, `ScoreScreen`  
├── **helper/** → `utilis.kt` (loads puzzle images from assets), `LocaleHelper` (language switching)  
├── **assets/1, /2, /3** → the 30 puzzle images, 10 per level  
└── **MainActivity.kt** → app entry point, navigation graph, bottom nav

## How It Works

1. On the Landing screen, the player enters a username and optionally picks a language.
2. On the Settings screen, the player picks a difficulty level (1–3).
3. `GameViewModel` picks 3 random puzzles from that level and starts a timer.
4. For each puzzle, the player types a numeric answer and taps **CHECK** — `GameViewModel` compares it against `PuzzleBank`'s answer key and shows a result dialog.
5. After the 3rd puzzle, the round's score, duration, and level are saved to Room and the player is taken to the Score screen, which lists every past round.

## Getting Started

1. Clone this repository.
2. Open the project in **Android Studio**.
3. Let Gradle sync (Kotlin 2.2.10, AGP 9.2.1, Room 2.8.4).
4. Run on an emulator or physical device (min SDK 26).

## Testing

`androidTest/java/com/example/eduapp/ExampleInstrumentedTest.kt` runs against a real, in-memory Room database (wiped after each test):

- `useAppContext` — sanity check on the app's package name
- `insert_thenReadBack_returnsSameValues` — insert a user, verify every field round-trips
- `getAllUsers_ordersNewestFirst` — verify the score list query returns newest-first
- `deleteAll_clearsEveryRow` — verify the table actually empties

Run via Android Studio (right-click the file → Run) or `./gradlew connectedAndroidTest`.

## Limitations

- **No cloud database** — score history is stored locally with Room only, so it doesn't sync across devices and is lost if the app is uninstalled or app data is cleared.
- **No user accounts** — "username" is just a free-text label typed each round, not an authenticated identity, so anyone can play under any name and there's no way to verify who a score belongs to.
- **No global leaderboard** — the score list only shows rounds played on that specific device.
- **Fixed puzzle set** — all 30 puzzles are bundled as assets at build time; adding or changing puzzles requires a new app build rather than a remote content update.
