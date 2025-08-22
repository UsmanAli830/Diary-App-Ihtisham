package com.example.diarywithlock.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.diarywithlock.components.DiaryBottomBar
import com.example.diarywithlock.database.Note
import com.example.diarywithlock.viewmodel.NoteViewModel
import java.time.LocalDate
import java.time.YearMonth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navController: NavController,
    viewModel: NoteViewModel,
    currentRoute: String
) {


    var noNotesMessage by remember { mutableStateOf("") }
    val notes by viewModel.notes.collectAsState()
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var displayedNotes by remember { mutableStateOf<List<Note>>(emptyList()) }

    val firstDayOfMonth = currentMonth.atDay(1)
    val firstWeekday = firstDayOfMonth.dayOfWeek.value % 7 // Sunday = 0
    val daysInMonth =
        (1..currentMonth.lengthOfMonth()).map { firstDayOfMonth.plusDays((it - 1).toLong()) }

// Add empty placeholders so the first day starts on the correct weekday
    val calendarCells = List(firstWeekday) { null } + daysInMonth


    // Weekday labels
    val weekDays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFE0B2), // soft peach at top
                        Color(0xFFBBDEFB)  // light blue at bottom
                    )
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
                            }

                            Text(
                                text = "${
                                    currentMonth.month.name.lowercase()
                                        .replaceFirstChar { it.uppercase() }
                                } ${currentMonth.year}",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black
                            )

                            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
                            }
                        }
                    }
                )
            },
            bottomBar = { DiaryBottomBar(navController, viewModel, currentRoute) }
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            )

            {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    weekDays.forEach { day ->
                        Text(
                            text = day,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier
                        .height(357.dp)
                        .padding(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(calendarCells) { date ->
                        if (date == null) {
                            Box(modifier = Modifier.size(48.dp)) {} // empty placeholder for alignment
                        } else {
                            val notesOnDate = notes.filter { note ->
                                try {
                                    LocalDate.parse(note.date) == date
                                } catch (_: Exception) {
                                    false
                                }
                            }.sortedBy { it.time }

                            val lastMood = notesOnDate.lastOrNull()?.mood
                            val notesWithMood = notesOnDate.filter { it.mood.isNotEmpty() }



                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = if (date == LocalDate.now()) 2.dp else 0.dp,
                                        color = Color(0xFF2196F3),
                                        shape = CircleShape
                                    )
                                    .background(
                                        when {
                                            notesWithMood.isNotEmpty() -> Color(0xFFFFF9C4) // notes with mood
                                            notesOnDate.isNotEmpty() -> Color(0xFF64B5F6)
                                            else -> Color.Transparent
                                        }
                                    )
                                    .clickable {
                                        if (notesOnDate.isEmpty()) {
                                            noNotesMessage = "No notes saved on this date"
                                            displayedNotes = emptyList()
                                        } else {
                                            val notesWithMood = notesOnDate.filter { it.mood.isNotEmpty() }

                                            displayedNotes = if (notesWithMood.isNotEmpty()) {

                                                notes.filter { it.mood == notesWithMood.last().mood }
                                            } else {

                                                notesOnDate.filter { it.mood.isEmpty() }
                                            }
                                            noNotesMessage = ""

                                        }
                                    },


                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (lastMood.isNullOrEmpty()) date.dayOfMonth.toString() else lastMood,
                                    fontSize = if (!lastMood.isNullOrEmpty()) 20.sp else 16.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                )



                            }

                        }

                    }

                }

                Spacer(modifier = Modifier.height(8.dp))

                if (displayedNotes.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        items(displayedNotes) { note ->
                            NoteItem(note) {}
                        }
                    }
                } else if (noNotesMessage.isNotEmpty()) {
                    Text(
                        text = noNotesMessage,
                        modifier = Modifier.padding(8.dp),
                        color = Color.Gray
                    )
                }

            }

        }
    }
}



