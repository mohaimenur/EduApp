package com.example.eduapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.eduapp.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

// Final screen: a table of every past game session, most recent first
// (ordering comes from AppDao's query - see database/AppDao.kt).
// Mirrors the reference app's "SCORE LIST" screen.
@Composable
fun ScoreScreen(
    navController: NavHostController,
    appViewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    // AppViewModel.users is a Flow from Room - collectAsStateWithLifecycle
    // turns it into Compose state and re-runs this composable automatically
    // whenever a new row is inserted (e.g. right after a game finishes).
    val users by appViewModel.users.collectAsStateWithLifecycle(initialValue = emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header row - weights must match the data row below so columns line up.
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Name", Modifier.weight(2f), fontWeight = FontWeight.Bold)
            Text("Level", Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("Score", Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("MM:SS", Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("Date", Modifier.weight(2f), fontWeight = FontWeight.Bold)
        }

        // LazyColumn only composes rows currently on screen, so this
        // stays cheap even with a long score history.
        LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
            items(users) { user ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(user.username, Modifier.weight(2f))
                    Text(user.level, Modifier.weight(1f))
                    Text("${user.score}", Modifier.weight(1f))
                    // duration is stored in whole seconds; format as M:SS.
                    Text(
                        "${user.duration / 60}:${(user.duration % 60).toString().padStart(2, '0')}",
                        Modifier.weight(1f)
                    )
                    Text(dateFormat.format(Date(user.date)), Modifier.weight(2f))
                }
            }
        }
    }
}
