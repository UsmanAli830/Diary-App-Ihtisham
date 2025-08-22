package com.example.diarywithlock.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diarywithlock.components.AudioRecordingDialog
import com.example.diarywithlock.utils.fontMap
import com.example.diarywithlock.utils.themeMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionBottomSheet(
    title: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss, // This handles outside clicks/back press
        sheetState = sheetState,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with close button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Options grid
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 60.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(options) { item ->
                    Text(
                        text = item,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .clickable { onOptionSelected(item) }
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}


