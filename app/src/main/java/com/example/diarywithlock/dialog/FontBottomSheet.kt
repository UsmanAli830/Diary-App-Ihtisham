package com.example.diarywithlock.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diarywithlock.utils.fontMap

@Composable
fun FontAndColorBottomSheet(
    selectedFont: String,
    selectedColor: Int,
    onFontSelected: (String) -> Unit,
    onColorSelected: (Int) -> Unit
) {
    val colors = listOf(
        0xFF000000.toInt(), // Black
        0xFFFF0000.toInt(), // Red
        0xFF00FF00.toInt(), // Green
        0xFF0000FF.toInt(), // Blue
        0xFFFFFF00.toInt(), // Yellow
        0xFFFFA500.toInt()  // Orange
    )

    Column(modifier = Modifier.padding(16.dp)) {
        // Row for colors
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            colors.forEach { colorInt ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = Color(colorInt), shape = CircleShape)
                        .border(
                            width = if (colorInt == selectedColor) 3.dp else 1.dp,
                            color = if (colorInt == selectedColor) Color.Gray else Color.LightGray,
                            shape = CircleShape
                        )
                        .clickable { onColorSelected(colorInt) }
                )
            }
        }

        // Fonts grid (3 columns)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
        ) {
            items(items = fontMap.keys.toList()) { fontName ->
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (fontName == selectedFont) Color.LightGray else Color.Transparent)
                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                        .clickable { onFontSelected(fontName.toString()) }
                        .padding(16.dp),  // Inner padding for the text
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = fontName,
                        fontFamily = fontMap[fontName] ?: FontFamily.Default,
                        color = Color.Black,
                        fontSize = 16.sp,  // Slightly smaller font size

                    )
                }
            }

            }
        }
    }