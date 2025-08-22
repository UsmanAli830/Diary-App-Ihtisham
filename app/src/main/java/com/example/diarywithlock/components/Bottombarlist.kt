package com.example.diarywithlock.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.EditLocation
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.TagFaces
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
 fun BottomBar(
    onListClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onEmojiClick: () -> Unit,
    onFontClick: () -> Unit,
    onBackgroundClick: () -> Unit,
    onHashTagClick: () -> Unit,
    onRecordingClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(8.dp)
            .background(Color.Red)
            .imePadding(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        ToolbarIcon(Icons.Default.Book, "Bookmark", onListClick)
        ToolbarIcon(Icons.Default.Image, "Gallery", onGalleryClick)
        ToolbarIcon(Icons.Default.CameraAlt, "Camera", onCameraClick)
        ToolbarIcon(Icons.Default.TagFaces, "Emoji", onEmojiClick)
        ToolbarIcon(Icons.Default.TextFields, "Font", onFontClick)
        ToolbarIcon(Icons.Default.Brightness6, "Background", onBackgroundClick)
        ToolbarIcon(Icons.Default.EditLocation, "hash tag", onHashTagClick)
        ToolbarIcon(Icons.Default.Mic, "Recording", onRecordingClick)

    }
}