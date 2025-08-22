package com.example.diarywithlock.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.diarywithlock.database.Note
import com.example.diarywithlock.navigation.NavRoutes
import com.example.diarywithlock.utils.fontMap
import com.example.diarywithlock.utils.themeMap
import com.example.diarywithlock.viewmodel.NoteViewModel
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.diarywithlock.R
import com.example.diarywithlock.components.DiaryBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    navController: NavController,
    viewModel: NoteViewModel,
    filterMood: String? = null
) {
    val allNotes = viewModel.notes.collectAsState().value
    val notes = if (filterMood != null) {
        allNotes.filter { it.mood == filterMood }
    } else allNotes

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route ?: ""



    Scaffold(
        topBar = { TopAppBar(title = { Text("My Diary") }) },
        bottomBar = { DiaryBottomBar(navController, viewModel, currentRoute) }
    ) { paddingValues ->
        if (notes.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No notes yet")
            }
        } else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(notes) { note ->
                    NoteItem(note = note) {
                        navController.navigate(NavRoutes.EditNote.createRoute(note.id))
                    }
                }
            }
        }
    }
}





@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(note: Note, onClick: () -> Unit) {
    val fontFamily = remember(note.fontStyle) { fontMap[note.fontStyle] ?: FontFamily.Default }
    val backgroundResId = remember(note.theme) { themeMap[note.theme] ?: R.drawable.background3 }
    val textColor = remember(note.textColor) { Color(note.textColor) }
    val audioNotes = remember(note.content) { Regex("""\[Audio:.*?]""").findAll(note.content).toList() }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .background(Color.Transparent)
    ) {
            // Async background
            AsyncImage(
                model = backgroundResId,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(12.dp))
            )

            // Mood
            Text(
                text = note.mood,
                fontSize = 24.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            )

            // Audio notes count
            if (audioNotes.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 40.dp, end = 8.dp)
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Mic, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("${audioNotes.size}", color = Color.White, fontSize = 14.sp)
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = fontFamily,
                        color = textColor
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = note.content,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = fontFamily,
                        color = textColor
                    )
                )

                Spacer(Modifier.height(8.dp))

                // Images: horizontal scroll instead of grid for performance
                if (note.images.isNotEmpty()) {
                    LazyRow(modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                    ) {
                        items(note.images) { imagePath ->
                            AsyncImage(
                                model = imagePath.toUri(),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(2.dp)
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }






