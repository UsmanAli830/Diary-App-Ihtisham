package com.example.diarywithlock.database

import android.content.Context
import androidx.room.Room

object NoteDatabaseInstance {

    @Volatile
    private var INSTANCE: NoteDatabase? = null

    fun getDatabase(context: Context): NoteDatabase {

        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                "note_db"
            )
                .fallbackToDestructiveMigration()
                .build()

            INSTANCE = instance
            instance
        }
    }
}
