package com.example.diarywithlock.components

import android.content.Context
import android.media.MediaRecorder
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun AudioRecordingDialog(
    context: Context,
    onSave: (fileName: String, path: String) -> Unit,
    onDismiss: () -> Unit
) {
    var isRecording by remember { mutableStateOf(true) }
    var recordingTime by remember { mutableStateOf(0) }
    var mediaRecorder: MediaRecorder? = remember { null }
    var tempFile by remember { mutableStateOf<File?>(null) }

    // Start recording when dialog opens
    LaunchedEffect(Unit) {
        tempFile = File(context.cacheDir, "audio_${System.currentTimeMillis()}.mp3")
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(tempFile!!.absolutePath)
            prepare()
            start()
        }
        while (isRecording) {
            delay(1000)
            recordingTime++
        }
    }

    var showFileNameDialog by remember { mutableStateOf(false) }
    var fileName by remember { mutableStateOf("") }

    if (!showFileNameDialog) {
        // Main recording dialog
        AlertDialog(
            onDismissRequest = {
                mediaRecorder?.apply { stop(); release() }
                tempFile?.delete()
                isRecording = false
                onDismiss()
            },
            title = { Text("Recording...") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = String.format("%02d:%02d", recordingTime / 60, recordingTime % 60))
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Button(onClick = {
                            // Restart recording
                            mediaRecorder?.apply { stop(); release() }
                            tempFile?.delete()
                            recordingTime = 0
                            tempFile = File(context.cacheDir, "audio_${System.currentTimeMillis()}.mp3")
                            mediaRecorder = MediaRecorder().apply {
                                setAudioSource(MediaRecorder.AudioSource.MIC)
                                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                                setOutputFile(tempFile!!.absolutePath)
                                prepare()
                                start()
                            }
                        }) { Text("Restart") }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(onClick = {
                            mediaRecorder?.apply { stop(); release() }
                            isRecording = false
                            showFileNameDialog = true
                        }) { Text("Save") }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    } else {
        // Filename input dialog
        AlertDialog(
            onDismissRequest = { showFileNameDialog = false },
            title = { Text("Save Audio") },
            text = {
                OutlinedTextField(
                    value = fileName,
                    onValueChange = { fileName = it },
                    label = { Text("File Name") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val finalFile = File(context.filesDir, "$fileName.mp3")
                    tempFile?.copyTo(finalFile, overwrite = true)
                    onSave(fileName, finalFile.absolutePath)
                    showFileNameDialog = false
                    onDismiss()
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = {
                    tempFile?.delete()
                    showFileNameDialog = false
                    onDismiss()
                }) { Text("Cancel") }
            }
        )
    }
}