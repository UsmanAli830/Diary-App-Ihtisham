package com.example.diarywithlock.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.example.diarywithlock.utils.themeMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackgroundSelectionBottomSheet(
    onBackgroundSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val imageIds = themeMap.values.toList()

    ModalBottomSheet(onDismissRequest = { onDismiss() }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Select Background",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(imageIds) { drawableRes ->
                    AsyncImage(
                        model = drawableRes,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onBackgroundSelected(drawableRes) }
                    )
                }
            }
        }
    }
}
