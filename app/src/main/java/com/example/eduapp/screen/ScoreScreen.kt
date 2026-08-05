package com.example.eduapp.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.eduapp.R
import com.example.eduapp.database.User
import com.example.eduapp.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.Locale

private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH)

@Composable
fun ScoreScreen(
    navController: NavHostController,
    appViewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val users by appViewModel.users.collectAsStateWithLifecycle(initialValue = emptyList())
    val scrollState = rememberScrollState()
    var showDeleteAllDialog by remember { mutableStateOf(false) }
    var userToEdit by remember { mutableStateOf<User?>(null) }
    var userToDelete by remember { mutableStateOf<User?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.score_history),
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
                TableCell(text = stringResource(R.string.table_name), weight = 1.8f, isHeader = true)
                VerticalDivider()
                TableCell(text = stringResource(R.string.table_lvl), weight = 0.7f, isHeader = true)
                VerticalDivider()
                TableCell(text = stringResource(R.string.table_score), weight = 1.2f, isHeader = true)
                VerticalDivider()
                TableCell(text = stringResource(R.string.table_time), weight = 1.0f, isHeader = true)
                VerticalDivider()
                TableCell(text = stringResource(R.string.actions), weight = 1.8f, isHeader = true)
            }

            // Data rows
            users.forEach { user ->
                HorizontalDivider(thickness = 0.5.dp, color = Color.Gray)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TableCell(text = user.username, weight = 1.8f)
                    VerticalDivider()
                    TableCell(text = user.level, weight = 0.7f)
                    VerticalDivider()
                    TableCell(text = String.format(Locale.ENGLISH, "%d", user.score), weight = 1.2f)
                    VerticalDivider()
                    TableCell(
                        text = String.format(Locale.ENGLISH, "%d:%02d", user.duration / 60, user.duration % 60),
                        weight = 1.0f
                    )
                    VerticalDivider()
                    
                    // Action Icons
                    Row(
                        modifier = Modifier.weight(1.8f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { userToEdit = user }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        }
                        IconButton(onClick = { userToDelete = user }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.padding(top = 24.dp, bottom = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    navController.navigate("setting") {
                        popUpTo("landing") { inclusive = false }
                    }
                }
            ) {
                Text(stringResource(R.string.play_again))
            }

            if (users.isNotEmpty()) {
                Button(
                    onClick = { showDeleteAllDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.clear_history))
                }
            }
        }
    }

    // Individual Edit Dialog
    userToEdit?.let { user ->
        var newName by remember { mutableStateOf(user.username) }
        AlertDialog(
            onDismissRequest = { userToEdit = null },
            title = { Text(stringResource(R.string.edit_username)) },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { if (it.length <= 15) newName = it },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    appViewModel.updateUser(user.copy(username = newName))
                    userToEdit = null
                }) {
                    Text(stringResource(R.string.update))
                }
            },
            dismissButton = {
                TextButton(onClick = { userToEdit = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    // Individual Delete Dialog
    userToDelete?.let { user ->
        AlertDialog(
            onDismissRequest = { userToDelete = null },
            title = { Text(stringResource(R.string.delete)) },
            text = { Text(stringResource(R.string.delete_item_confirmation)) },
            confirmButton = {
                TextButton(onClick = {
                    appViewModel.deleteUser(user)
                    userToDelete = null
                }) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { userToDelete = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    // Delete All Confirmation
    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAllDialog = false },
            title = { Text(stringResource(R.string.delete_confirmation_title)) },
            text = { Text(stringResource(R.string.delete_confirmation_message)) },
            confirmButton = {
                TextButton(onClick = {
                    appViewModel.clearUsers()
                    showDeleteAllDialog = false
                }) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAllDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
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
        maxLines = 1,
        fontSize = 13.sp
    )
}
