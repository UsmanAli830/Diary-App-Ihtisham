package com.example.diarywithlock.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.diarywithlock.viewmodel.NoteViewModel
import com.example.diarywithlock.navigation.NavRoutes

@Composable
fun DiaryBottomBar(navController: NavController, viewModel: NoteViewModel,  currentRoute: String) {
    val baseRoute = currentRoute.substringBefore("?")


    BottomAppBar {
        IconButton(onClick = {

            if (baseRoute != NavRoutes.NoteList.route) {
                navController.navigate(NavRoutes.NoteList.route) {
                    launchSingleTop = true
                    popUpTo(NavRoutes.NoteList.route) { inclusive = false }
                }
            }

        }) { Icon(Icons.Filled.List, contentDescription = "List") }




        IconButton(onClick = {
            if (baseRoute != NavRoutes.Calendar.route) {
                navController.navigate(NavRoutes.Calendar.route) {
                    launchSingleTop = true
                }
            }
        }) {
            Icon(Icons.Filled.DateRange, contentDescription = "Calendar")
        }


        Spacer(modifier = Modifier.weight(1f))

        // custom add button
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color(0xFFFF4081))
                .clickable {
                    navController.navigate(NavRoutes.AddNote.route)
                    viewModel.clearCurrentNote()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Note",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.weight(1f))


        IconButton(onClick = {
            if (baseRoute != NavRoutes.Gallery.route) {
                navController.navigate(NavRoutes.Gallery.route) {
                    launchSingleTop = true
                }
            }
        }) {
            Icon(Icons.Filled.Photo, contentDescription = "Gallery")
        }


        IconButton(onClick = {
            if (baseRoute != NavRoutes.Settings.route) {
                navController.navigate(NavRoutes.Settings.route) {
                    launchSingleTop = true
                }
            }
        }) {
            Icon(Icons.Filled.Settings, contentDescription = "Settings")
        }

    }
}
