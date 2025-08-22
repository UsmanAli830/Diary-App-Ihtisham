package com.example.diarywithlock.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diarywithlock.database.Note
import com.example.diarywithlock.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    private val _currentNote = MutableStateFlow<Note?>(null)
    val currentNote: StateFlow<Note?> = _currentNote



    fun getNoteById(noteId: Int) {
        viewModelScope.launch {
            repository.getNoteById(noteId).collect {
                _currentNote.value = it
            }
        }
    }


    val notes = repository.getNotes()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    private val _currentMood = MutableStateFlow<String>("")
    val currentMood: StateFlow<String> = _currentMood
    private val _showMoodBottomSheet = MutableStateFlow(false)
    val showMoodBottomSheet: StateFlow<Boolean> = _showMoodBottomSheet
    private val _moods = MutableStateFlow(listOf("😡", "😢", "😊", "😐", "😍", "🤔"))
    val currentMoods: StateFlow<List<String>> = _moods
    private val _selectedMood = MutableStateFlow("")
    val selectedMood: StateFlow<String> = _selectedMood


    fun addNote(
        title: String,
        content: String,
        date: String,
        time: String,
        theme: String,
        fontStyle: String,
        images: List<String>,
        mood: String,
        emoji: String,
        textColor: Int,
        hashtags : List<String>,
        audioPaths: List<String>
    ) {
        val note = Note(
            title = title,
            content = content,
            images = images,
            date = date,
            time = time,
            fontStyle = fontStyle,
            theme = theme,
            textColor = textColor,
            mood = mood,
            emoji = emoji,
            hashtags = hashtags,
            audioPaths = audioPaths
        )

        viewModelScope.launch {
            repository.insert(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.update(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }

    fun clearCurrentNote() {
        _currentNote.value = null
    }
    fun updateMood(mood: String) {
        _currentMood.value = mood
    }
    fun triggerMoodBottomSheet() {
        _showMoodBottomSheet.value = true
    }

    fun resetMoodBottomSheetTrigger() {
        _showMoodBottomSheet.value = false
    }
    fun updateMoods(newMoods: List<String>) {
        _moods.value = newMoods
    }
    fun setMood(mood: String) {
        _selectedMood.value = mood
    }



}

