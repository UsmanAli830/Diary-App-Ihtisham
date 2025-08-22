package com.example.diarywithlock.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.diarywithlock.viewmodel.NoteViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MoodGalleryScreen(
    navController: NavController,
    noteViewModel: NoteViewModel
) {
    val moodCards = listOf(
        MoodCard("Default", listOf("😄","😊","😍","🙂","😲","😡","😢","😭")),
        MoodCard("Shiba-Inu", listOf("🐶","🐕","🦊","🐺","🐾","🐩","🐕‍🦺","🦝")),
        MoodCard("Mousey", listOf("🐭","🐁","🐀","🐹","🐰","🐿️","🦔","🦦"))
        // Add more cards here
    )

    var selectedCardIndex by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Mood gallery",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(moodCards) { index, card ->
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .clickable { selectedCardIndex = index },
                    border = if (selectedCardIndex == index) BorderStroke(2.dp, Color.Red) else null,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = card.name, style = MaterialTheme.typography.bodyMedium)
                        Row(modifier = Modifier.padding(top = 8.dp)) {
                            card.moods.forEach { mood ->
                                Text(
                                    text = mood,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .size(40.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                            shape = CircleShape
                                        )
                                        .wrapContentSize(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        val newMoods = moodCards[selectedCardIndex].moods
                        noteViewModel.updateMood(newMoods.first())
                        noteViewModel.updateMoods(newMoods)
                        noteViewModel.triggerMoodBottomSheet()
                        navController.popBackStack()
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Apply")
                }
            }
        }
    }
}

data class MoodCard(
    val name: String,
    val moods: List<String>
)


