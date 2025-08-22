package com.example.diarywithlock.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val date : String,
    val time : String,
    val theme : String,
    val fontStyle : String,
    val images: List<String>,
    val mood : String,
    val emoji : String,
    val textColor : Int,
    val hashtags : List<String>,
    val audioPaths: List<String> = emptyList()

)
