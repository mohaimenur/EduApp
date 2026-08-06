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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.eduapp.R

// Maps 1:1 with asset folders "1", "2", and "3"
private val LEVELS = listOf("1", "2", "3")

/**
 * Screen for selecting game difficulty level.
 * Adapts its layout to be vertically centered and potentially side-by-side in landscape.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavHostController,
    selectedLevel: String,
    onLevelChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Dropdown expanded state preserved during rotation
    var levelExpanded by rememberSaveable { mutableStateOf(value = false) }
    
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Layout padding adjustment for portrait mode
        if (!isLandscape) {
            Spacer(Modifier.height(40.dp))
        }

        // Instruction label with dynamic alignment
        Text(
            text = stringResource(R.string.select_level),
            modifier = Modifier
                .fillMaxWidth(if (isLandscape) 0.8f else 1f)
                .padding(horizontal = if (isLandscape) 16.dp else 0.dp),
            textAlign = if (isLandscape) TextAlign.Start else TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )

        if (isLandscape) {
            // Horizontal row layout for efficient space usage in landscape
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
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
            // Vertical stacked layout for portrait mode
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

/**
 * Reusable dropdown component for selecting a difficulty level.
 * Uses Material 3 ExposedDropdownMenuBox.
 */
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
