package com.example.diarywithlock.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.diarywithlock.screens.NoteListScreen
import com.example.diarywithlock.screens.AddEditNoteScreen
import com.example.diarywithlock.screens.CalendarScreen
import com.example.diarywithlock.screens.GalleryScreen
import com.example.diarywithlock.screens.MoodGalleryScreen
import com.example.diarywithlock.screens.SettingScreen
import com.example.diarywithlock.viewmodel.NoteViewModel
import java.nio.charset.StandardCharsets

@Composable
fun DiaryNavHost(
    navController: NavHostController,
    viewModel: NoteViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.NoteList.route,
        modifier = modifier
    ) {
        composable(
            route = "${NavRoutes.NoteList.route}?filterMood={filterMood}",
            arguments = listOf(
                navArgument("filterMood") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val filterMood = backStackEntry.arguments?.getString("filterMood")?.let {
                java.net.URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            }
            NoteListScreen(navController, viewModel, filterMood = filterMood)
        }




        composable(NavRoutes.AddNote.route) {
            AddEditNoteScreen(navController, viewModel)
        }

        composable(
            route = NavRoutes.EditNote.route,
            arguments = listOf(navArgument("noteId") { type = NavType.IntType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId")
            if (noteId != null) {
                AddEditNoteScreen(navController, viewModel, noteId = noteId)
            }
        }

        composable(NavRoutes.MoodGalleryScreen.route) {
            MoodGalleryScreen(navController, noteViewModel = viewModel)
        }

        composable(NavRoutes.Calendar.route) {
            val navBackStackEntry = navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry.value?.destination?.route ?: ""
            CalendarScreen(navController, viewModel, currentRoute)
        }


        composable(NavRoutes.Gallery.route) {
            val currentRoute = NavRoutes.Gallery.route
            GalleryScreen(
                navController = navController,
                viewModel = viewModel,
                currentRoute = currentRoute
            )
        }


        composable(NavRoutes.Settings.route) {
            val currentRoute = NavRoutes.Settings.route
            SettingScreen(
                navController,
                viewModel = viewModel,
                currentRoute = currentRoute
            )
        }







    }
}
