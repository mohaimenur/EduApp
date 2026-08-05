package com.example.eduapp.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
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
    var levelExpanded by rememberSaveable { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Content will be centered vertically
    ) {
        if (!isLandscape) {
            Spacer(Modifier.height(40.dp))
        }

        Text(
            text = stringResource(R.string.select_level),
            modifier = Modifier
                .fillMaxWidth(if (isLandscape) 0.8f else 1f)
                .padding(horizontal = if (isLandscape) 16.dp else 0.dp),
            textAlign = if (isLandscape) TextAlign.Start else TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f) // Don't take full width in landscape to keep it centered and tidy
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    LevelDropdown(
                        expanded = levelExpanded,
                        onExpandedChange = { levelExpanded = it },
                        selectedLevel = selectedLevel,
                        onLevelChange = onLevelChange
                    )
                }
                Button(
                    onClick = { navController.navigate("game") },
                    modifier = Modifier.weight(0.5f)
                ) { Text(stringResource(R.string.go)) }
            }
        } else {
            LevelDropdown(
                expanded = levelExpanded,
                onExpandedChange = { levelExpanded = it },
                selectedLevel = selectedLevel,
                onLevelChange = onLevelChange
            )

            Button(
                onClick = { navController.navigate("game") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) { Text(stringResource(R.string.go)) }

            Spacer(Modifier.height(40.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelDropdown(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedLevel: String,
    onLevelChange: (String) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = Modifier.padding(top = 8.dp)
    ) {
        OutlinedTextField(
            value = stringResource(R.string.level_label, selectedLevel),
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            LEVELS.forEach { lvl ->
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.level_label, lvl)) },
                    onClick = {
                        onLevelChange(lvl)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}
