package com.example.diarywithlock.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
 fun TopBar(
    navController: NavController,
    selectedMood: String,
    onMoodClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ToolbarIcon(Icons.AutoMirrored.Filled.ArrowBack, "Back") { navController.popBackStack() }
        Row {
            IconButton(onClick = onMoodClick) {
                if (selectedMood.isEmpty()) {
                    // Show placeholder icon when no mood is selected
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Select Mood"
                    )
                } else {
                    // Show selected mood text/emoji
                    Text(
                        text = selectedMood,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            ToolbarIcon(Icons.Default.Save, "Save", onSaveClick)
        }
    }
}