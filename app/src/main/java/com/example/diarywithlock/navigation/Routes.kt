package com.example.diarywithlock.navigation


sealed class NavRoutes(val route: String) {

    // Start screen: note list
    object NoteList : NavRoutes("note_list")

    // Add new note screen
    object AddNote : NavRoutes("add_note")

    // Edit existing note screen (noteId passed in route)
    object EditNote : NavRoutes("edit_note/{noteId}") {
        fun createRoute(noteId: Int): String = "edit_note/$noteId"
    }
    object MoodGalleryScreen : NavRoutes ("mood_gallery")

    object Calendar : NavRoutes("calendar")
    object Gallery : NavRoutes("gallery")
    object Settings : NavRoutes("settings")


}
