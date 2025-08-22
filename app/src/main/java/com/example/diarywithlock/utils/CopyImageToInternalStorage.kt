package com.example.diarywithlock.utils
import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun copyImageToInternalStorage(context: Context, uri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(uri)
    val fileName = "img_${System.currentTimeMillis()}.jpg"
    val file = File(context.filesDir, fileName)
    val outputStream = FileOutputStream(file)
    inputStream?.copyTo(outputStream)
    inputStream?.close()
    outputStream.close()
    return file.absolutePath
}
