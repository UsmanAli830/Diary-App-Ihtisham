package com.example.diarywithlock.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.diarywithlock.components.DiaryBottomBar
import com.example.diarywithlock.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    navController: NavHostController,
    viewModel: NoteViewModel,
    currentRoute: String
) {
    val notes by viewModel.notes.collectAsState()

    // Flatten all images from all notes into a single list
    val allImages = notes.flatMap { it.images }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Gallery") })
        },
        bottomBar = { DiaryBottomBar(navController, viewModel, currentRoute) }
    ) { paddingValues ->
        if (allImages.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("No images yet")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allImages) { imagePath ->
                    Image(
                        painter = rememberAsyncImagePainter(imagePath),
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable {
                                // Optional: navigate to full-screen view of image
                            },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
