package com.example.diarywithlock.dialog

import android.R
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.emoji2.emojipicker.EmojiPickerView

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmojiBottomSheet(
    textFieldState: MutableState<String>,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    ModalBottomSheet(onDismissRequest = onDismiss) {
         AndroidView(
                factory = {
                    EmojiPickerView(context).apply {
                        setOnEmojiPickedListener { emoji ->
                            textFieldState.value += emoji.emoji
                        }

                        setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.7f)

            )
        }
    }


