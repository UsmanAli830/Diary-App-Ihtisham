package com.example.diarywithlock.utils

import android.content.Intent
import android.os.Build
import android.provider.MediaStore

fun buildGalleryIntent(): Intent {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        Intent(MediaStore.ACTION_PICK_IMAGES).apply {
            putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, 5)
        }
    } else {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
        }
    }
}