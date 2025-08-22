package com.example.diarywithlock.dialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.diarywithlock.navigation.NavRoutes
import com.example.diarywithlock.viewmodel.NoteViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodBottomSheet(
    navController: NavController,
    noteViewModel: NoteViewModel,
    onDismiss: () -> Unit
) {
    val selectedMood by noteViewModel.currentMood.collectAsState()
    val moods by noteViewModel.currentMoods.collectAsState(initial = listOf("😡", "😢", "😊", "😐", "😍", "🤔"))


    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Title
            Text(
                text = "How are you today?",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 20.dp),
                fontWeight = FontWeight.Bold
            )

            // Mood selection row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                moods.forEach { mood ->
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(
                                if (mood == selectedMood) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                else MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                            .clickable {
                                noteViewModel.updateMood(mood)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = mood, fontSize = 30.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // "See more moods" button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        navController.navigate(NavRoutes.MoodGalleryScreen.route)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "See more moods",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
