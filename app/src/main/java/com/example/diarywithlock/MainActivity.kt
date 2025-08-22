package com.example.diarywithlock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.ViewModelProvider
import com.example.diarywithlock.database.NoteDatabaseInstance
import com.example.diarywithlock.navigation.DiaryNavHost
import com.example.diarywithlock.repository.NoteRepository
import com.example.diarywithlock.ui.theme.DiaryWithLockTheme
import com.example.diarywithlock.viewmodel.NoteViewModel
import com.example.diarywithlock.viewmodel.NoteViewModelFactory

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = NoteDatabaseInstance.getDatabase(applicationContext)
        val repository = NoteRepository(database.noteDao())
        val viewModelFactory = NoteViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[NoteViewModel::class.java]

        setContent {
            DiaryWithLockTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    DiaryNavHost(navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}
