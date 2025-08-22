package com.example.diarywithlock.repository

import com.example.diarywithlock.database.Note
import com.example.diarywithlock.database.NoteDao
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val dao: NoteDao) {

    fun getNotes() = dao.getAllNotes()
    fun getNoteById(id: Int): Flow<Note?> = dao.getNoteById(id)
    suspend fun insert(note: Note) = dao. insertNote(note)
    suspend fun update (note: Note) = dao.updateNote(note)
    suspend fun delete (note: Note) = dao.deleteNote(note)

}
