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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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

@Composable
fun ScoreScreen(
    navController: NavHostController,
    appViewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val users by appViewModel.users.collectAsStateWithLifecycle(initialValue = emptyList())
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Reduced top spacer so it doesn't push the table off screen
        Spacer(Modifier.height(20.dp))

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
                TableCell(text = "Name", weight = 2.2f, isHeader = true)
                VerticalDivider()
                TableCell(text = "Lvl", weight = 0.8f, isHeader = true)
                VerticalDivider()
                TableCell(text = "Score", weight = 1.5f, isHeader = true)
                VerticalDivider()
                TableCell(text = "Time", weight = 1.2f, isHeader = true)
                VerticalDivider()
                TableCell(text = "Date", weight = 2.5f, isHeader = true)
            }

            // Data rows
            users.forEach { user ->
                HorizontalDivider(thickness = 0.5.dp, color = Color.Gray)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    TableCell(text = user.username, weight = 2.2f)
                    VerticalDivider()
                    TableCell(text = user.level, weight = 0.8f)
                    VerticalDivider()
                    TableCell(text = "${user.score}", weight = 1.5f)
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

        Button(
            onClick = {
                navController.navigate("setting") {
                    popUpTo("landing") { inclusive = false }
                }
            },
            modifier = Modifier.padding(top = 24.dp, bottom = 40.dp)
        ) {
            Text("PLAY AGAIN")
        }
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
            .padding(4.dp),
        fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
        textAlign = TextAlign.Center,
        maxLines = 1
    )
}
