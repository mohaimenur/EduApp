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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.eduapp.R

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
    var levelExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))

        Text(stringResource(R.string.select_level))

        ExposedDropdownMenuBox(
            expanded = levelExpanded,
            onExpandedChange = { levelExpanded = it },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            OutlinedTextField(
                value = stringResource(R.string.level_label, selectedLevel),
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = levelExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
            )
            ExposedDropdownMenu(
                expanded = levelExpanded,
                onDismissRequest = { levelExpanded = false }
            ) {
                LEVELS.forEach { lvl ->
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.level_label, lvl)) },
                        onClick = {
                            onLevelChange(lvl)
                            levelExpanded = false
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
        ) { Text(stringResource(R.string.go)) }

        Spacer(Modifier.weight(2f))
    }
}
