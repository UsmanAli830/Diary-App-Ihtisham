package com.example.diarywithlock.utils



import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*

@Composable
fun rememberImagePickerLaunchers(
    context: Context,
    imageUris: MutableList<Uri>
): ImagePickerLaunchers {
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val intent = result.data
            val sdkInt = android.os.Build.VERSION.SDK_INT
            if (sdkInt >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                val clipData = intent?.clipData
                val uri = intent?.data
                if (clipData != null) {
                    for (i in 0 until clipData.itemCount) {
                        val originalUri = clipData.getItemAt(i).uri
                        val savedPath = copyImageToInternalStorage(context, originalUri)
                        imageUris.add(Uri.parse(savedPath))
                    }
                } else if (uri != null) {
                    val savedPath = copyImageToInternalStorage(context, uri)
                    imageUris.add(Uri.parse(savedPath))
                }
            } else {
                val uri = intent?.data
                uri?.let {
                    val savedPath = copyImageToInternalStorage(context, it)
                    imageUris.add(Uri.parse(savedPath))
                }
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri != null) {
            val savedPath = copyImageToInternalStorage(context, photoUri!!)
            imageUris.add(Uri.parse(savedPath))
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            photoUri = createImageUri(context)
            photoUri?.let { cameraLauncher.launch(it) }
        } else {
            android.widget.Toast.makeText(context, "Camera permission denied", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    return ImagePickerLaunchers(galleryLauncher, cameraLauncher, cameraPermissionLauncher, photoUri) { uri -> photoUri = uri }
}

data class ImagePickerLaunchers(
    val galleryLauncher: androidx.activity.result.ActivityResultLauncher<android.content.Intent>,
    val cameraLauncher: androidx.activity.result.ActivityResultLauncher<Uri>,
    val cameraPermissionLauncher: androidx.activity.result.ActivityResultLauncher<String>,
    val photoUri: Uri?,
    val setPhotoUri: (Uri?) -> Unit
)
