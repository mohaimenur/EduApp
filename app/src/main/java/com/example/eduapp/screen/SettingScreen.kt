package com.example.eduapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

// The 3 difficulty levels correspond 1:1 with the "1"/"2"/"3" asset folders
// and PuzzleBank.forLevel() - keep these in sync if a level is ever added.
private val LEVELS = listOf("1", "2", "3")

// Second screen: pick a difficulty level, then start the game. Mirrors the
// reference app's level dropdown + GO button.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavHostController,
    selectedLevel: String,
    onLevelChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Only controls whether the dropdown list is open - not part of the
    // shared app state, so it's fine as local screen state.
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))

        Text("Select a level")

        // Standard Material3 dropdown pattern: a read-only text field
        // that opens a menu of choices when tapped.
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            OutlinedTextField(
                value = "Level $selectedLevel",
                onValueChange = {}, // read-only: text only changes via the menu below
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true) // anchors the dropdown menu to this field
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                LEVELS.forEach { lvl ->
                    DropdownMenuItem(
                        text = { Text("Level $lvl") },
                        onClick = {
                            onLevelChange(lvl) // reports the pick back up to AppNav
                            expanded = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = { navController.navigate("game") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) { Text("GO") }

        Spacer(Modifier.weight(2f))
    }
}
