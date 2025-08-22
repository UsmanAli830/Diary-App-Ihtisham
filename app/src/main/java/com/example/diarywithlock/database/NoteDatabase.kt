package com.example.diarywithlock.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Note::class], version = 4)
@TypeConverters(Converters::class)
abstract class NoteDatabase : RoomDatabase() {
        abstract fun noteDao(): NoteDao
}
