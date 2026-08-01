package com.example.eduapp.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(0.2f))

        Text(
            text = "SCORE HISTORY",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // The Table Container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray)
        ) {
            // Header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .border(0.5.dp, Color.Gray)
            ) {
                TableCell(text = "Name", weight = 2f, isHeader = true)
                VerticalDivider()
                TableCell(text = "Lvl", weight = 0.8f, isHeader = true)
                VerticalDivider()
                TableCell(text = "Score", weight = 1.2f, isHeader = true)
                VerticalDivider()
                TableCell(text = "Time", weight = 1.2f, isHeader = true)
                VerticalDivider()
                TableCell(text = "Date", weight = 2.5f, isHeader = true)
            }

            // LazyColumn for the data rows
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
            ) {
                items(users) { user ->
                    HorizontalDivider(thickness = 0.5.dp, color = Color.Gray)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                    ) {
                        TableCell(text = user.username, weight = 2f)
                        VerticalDivider()
                        TableCell(text = user.level, weight = 0.8f)
                        VerticalDivider()
                        TableCell(text = "${user.score}", weight = 1.2f)
                        VerticalDivider()
                        TableCell(
                            text = "${user.duration / 60}:${(user.duration % 60).toString().padStart(2, '0')}",
                            weight = 1.2f
                        )
                        VerticalDivider()
                        TableCell(text = dateFormat.format(Date(user.date)), weight = 2.5f)
                    }
                }
            }
        }

        Spacer(Modifier.weight(0.5f))
    }
}

@Composable
fun VerticalDivider() {
    Spacer(
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
            .border(0.5.dp, Color.Gray)
    )
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    isHeader: Boolean = false
) {
    Text(
        text = text,
        modifier = Modifier
            .weight(weight)
            .padding(8.dp),
        fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
        textAlign = TextAlign.Center,
        maxLines = 1
    )
}
