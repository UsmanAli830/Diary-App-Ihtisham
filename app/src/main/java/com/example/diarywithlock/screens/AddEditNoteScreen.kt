package com.example.diarywithlock.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.diarywithlock.R
import com.example.diarywithlock.database.Note
import com.example.diarywithlock.navigation.NavRoutes
import com.example.diarywithlock.dialog.ShowDatePickerDialog
import com.example.diarywithlock.utils.*
import com.example.diarywithlock.viewmodel.NoteViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.content.ContextCompat
import com.example.diarywithlock.components.AudioPlayerInline
import com.example.diarywithlock.components.AudioRecordingDialog
import com.example.diarywithlock.components.BottomBar
import com.example.diarywithlock.components.HighlightedContentTextField
import com.example.diarywithlock.components.NoteTextField
import com.example.diarywithlock.components.TopBar
import com.example.diarywithlock.dialog.BackgroundSelectionBottomSheet
import com.example.diarywithlock.dialog.EmojiBottomSheet
import com.example.diarywithlock.dialog.FontAndColorBottomSheet
import com.example.diarywithlock.dialog.HashtagBottomSheet
import com.example.diarywithlock.dialog.MoodBottomSheet
import com.example.diarywithlock.dialog.SelectionBottomSheet
import java.io.File
import java.time.LocalTime


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: NoteViewModel,
    noteId: Int? = null
) {

    val context = LocalContext.current
    val imageUris = remember { mutableStateListOf<Uri>() }

    var currentDialog by remember { mutableStateOf<SelectionBottomSheet?>(null) }

    val showMoodBottomSheet by viewModel.showMoodBottomSheet.collectAsState()
    LaunchedEffect(showMoodBottomSheet) {
        if (showMoodBottomSheet) {
            currentDialog = SelectionBottomSheet.Mood
            viewModel.resetMoodBottomSheetTrigger()
        }
    }


    var selectedFont by remember { mutableStateOf("wasted_vineyard") }
    var background by remember { mutableStateOf("background1") }
    var showFontSheet by remember { mutableStateOf(false) }

    val currentNote by viewModel.currentNote.collectAsState()
    val selectedMood by viewModel.currentMood.collectAsState(initial = currentNote?.mood ?: "😊")

    LaunchedEffect(noteId) {
        if (noteId == null) {
            viewModel.setMood("") // reset → placeholder icon shows
        } else {
            viewModel.getNoteById(noteId)
        }
    }




    LaunchedEffect(noteId) {
        noteId?.let { viewModel.getNoteById(it) }
    }

    if (noteId != null && currentNote == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val audioPathMap = remember { mutableStateMapOf<String, String>() }
    // Load note data from DB
    LaunchedEffect(currentNote) {
        currentNote?.let {
            selectedFont = it.fontStyle
            background = it.theme
            imageUris.clear()
            imageUris.addAll(it.images.map(Uri::parse))
            audioPathMap.clear()
            it.audioPaths.forEach { path ->
                val fileName = File(path).nameWithoutExtension
                audioPathMap[fileName] = path
            }
        }
    }

    var title by remember { mutableStateOf(TextFieldValue(currentNote?.title ?: "")) }
    var content by remember { mutableStateOf(TextFieldValue(currentNote?.content ?: "")) }
    var fontStyle by remember { mutableStateOf(selectedFont) }
    var textColor by remember { mutableIntStateOf(currentNote?.textColor ?: 0xFF000000.toInt()) }
    var selectedEmoji by remember { mutableStateOf(currentNote?.emoji ?: "") }
    var selectedImageIndex by remember { mutableIntStateOf(-1) }

    var selectedDate by remember {
        mutableStateOf(
            currentNote?.date?.let {
                runCatching { LocalDate.parse(it) }.getOrDefault(LocalDate.now())
            } ?: LocalDate.now()
        )
    }

    val fontFamily = fontMap[fontStyle] ?: FontFamily.Default
    val backgroundId = themeMap[background] ?: R.drawable.background3

    val (galleryLauncher, cameraLauncher, cameraPermissionLauncher, _, setPhotoUri) =
        rememberImagePickerLaunchers(context, imageUris)
    val hashtags = listOf("#Love", "#Happy", "#Travel", "#Food", "#Fitness", "#Inspiration")
    val recordPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Microphone permission is required", Toast.LENGTH_SHORT).show()
        }
    }



    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                selectedMood = selectedMood,
                onMoodClick = { currentDialog = SelectionBottomSheet.Mood },


                        onSaveClick = {
                            val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                            val note = Note(
                                id = noteId ?: 0,
                                title = title.text,
                                content = content.text,
                                date = selectedDate.toString(),
                                time = currentTime,
                                fontStyle = fontStyle,
                                theme = background,
                                images = imageUris.map { it.toString() },
                                mood = selectedMood,
                                emoji = selectedEmoji,
                                textColor = textColor,
                                hashtags = hashtags,
                                audioPaths = audioPathMap.values.toList()
                            )

                            if (noteId == null) {
                        viewModel.addNote(
                            note.title,
                            note.content,
                            note.date,
                            note.time,
                            note.theme,
                            note.fontStyle,
                            note.images,
                            note.mood,
                            note.emoji,
                            note.textColor,
                            note.hashtags,
                            note.audioPaths
                        )
                    } else {
                        viewModel.updateNote(note)
                    }
                    navController.popBackStack()
                }
            )
            if (currentDialog is SelectionBottomSheet.DatePicker) {
                ShowDatePickerDialog(
                    selectedDate = selectedDate,
                    onDateSelected = { date ->
                        selectedDate = date
                        currentDialog = null
                    },
                    onDismissRequest = {
                        currentDialog = null
                    }
                )

            }

        },
        bottomBar = {
            BottomBar(
                onGalleryClick = { galleryLauncher.launch(buildGalleryIntent()) },
                onCameraClick = {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val uri = createImageUri(context)
                        setPhotoUri(uri)
                        uri?.let { cameraLauncher.launch(it) }
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                onEmojiClick = { currentDialog = SelectionBottomSheet.Emoji },
                onFontClick = { showFontSheet = true },
                onBackgroundClick = { currentDialog = SelectionBottomSheet.Background },
                onListClick = { navController.navigate(NavRoutes.NoteList.route) },
                onHashTagClick = {
                    currentDialog = SelectionBottomSheet.HashTag
                },
                onRecordingClick = {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        // Ask for permission if not granted
                        recordPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    } else {
                        // Open the recording dialog
                        currentDialog = SelectionBottomSheet.AudioRecording
                    }
                }
            )


        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Image(
                painter = painterResource(id = backgroundId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = selectedDate.format(DateTimeFormatter.ofPattern("dd MMM, yyyy\nEEEE")),
                    color = Color(textColor),
                    fontFamily = fontFamily,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.clickable { currentDialog = SelectionBottomSheet.DatePicker }
                )

                Spacer(modifier = Modifier.height(16.dp))

                NoteTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "Title",
                    fontFamily = fontFamily,
                    textColor = textColor,
                    textStyle = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                HighlightedContentTextField(
                    text = content,
                    onValueChange = { content = it },
                    placeholder = "Start writing...",
                    fontFamily = fontFamily,
                    textColor = textColor,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.height(300.dp)
                )
                // Show audio notes
                // Render content with audio buttons
                val lines = content.text.split("\n")
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    lines.forEach { line ->
                        if (line.startsWith("[Audio:") && line.endsWith("]")) {
                            val fileName = line.removePrefix("[Audio:").removeSuffix("]")
                            val path = audioPathMap[fileName]
                            if (path != null) {
                                AudioPlayerInline(path)
                            } else {
                                Text(text = "[Audio file missing]", color = Color.Red)
                            }
                        } else {
                            Text(text = line, color = Color.Black)
                        }
                    }
                }



                Spacer(modifier = Modifier.height(16.dp))


                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                )
                {
                    items(imageUris.size) { index ->
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(100.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUris[index]),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        selectedImageIndex =
                                            if (selectedImageIndex == index) -1 else index
                                    },
                                contentScale = ContentScale.Crop
                            )

                            // Show delete icon only if this image is selected
                            if (selectedImageIndex == index) {
                                IconButton(
                                    onClick = {
                                        imageUris.removeAt(index)
                                        selectedImageIndex = -1
                                    },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(24.dp)
                                        .background(
                                            color = Color.Black.copy(alpha = 0.6f),
                                            shape = CircleShape
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Delete Image",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                if (currentDialog != null && currentDialog !is SelectionBottomSheet.DatePicker) {
                    when (currentDialog) {
                        is SelectionBottomSheet.Mood -> MoodBottomSheet(
                            navController = navController,
                            noteViewModel = viewModel,
                            onDismiss = { currentDialog = null }
                        )



                        is SelectionBottomSheet.Background -> {
                            ModalBottomSheet(
                                onDismissRequest = { currentDialog = null },
                                containerColor = Color.White
                            ) {
                                BackgroundSelectionBottomSheet(
                                    onBackgroundSelected = { selectedDrawableRes ->
                                        background = themeMap.entries
                                            .firstOrNull { it.value == selectedDrawableRes }
                                            ?.key ?: "background1"

                                    },
                                    onDismiss = { currentDialog = null }
                                )
                            }
                        }

                        is SelectionBottomSheet.Emoji -> {
                            val textState = remember { mutableStateOf(content.text) }

                            EmojiBottomSheet (
                                textFieldState = textState,
                                onDismiss = { currentDialog = null } // dismiss bottom sheet properly
                            )

                            // Keep content updated as user inserts emoji
                            LaunchedEffect(textState.value) {
                                content = content.copy(text = textState.value)
                            }
                        }


                        is SelectionBottomSheet.HashTag -> HashtagBottomSheet(
                            hashtags = hashtags,
                            onHashtagSelected = { selected ->
                                content = content.copy(
                                    text = content.text + " $selected",
                                    selection = TextRange((content.text + " $selected").length)
                                )

                            },
                            onDismiss = { currentDialog = null }
                        )

                        is SelectionBottomSheet.AudioRecording -> AudioRecordingDialog(
                            context = LocalContext.current,
                            onSave = { fileName, path ->
                                audioPathMap[fileName] = path
                                content = content.copy(
                                    text = content.text + "\n[Audio:$fileName]",
                                    selection = TextRange(content.text.length + fileName.length + 8)
                                )
                                currentDialog = null
                            },
                            onDismiss = { currentDialog = null }
                        )
                        is SelectionBottomSheet.DatePicker -> {} // do nothing
                        else -> {} // safety fallback
                    }
                }
                if (showFontSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showFontSheet = false },
                        containerColor = Color.White
                    ) {
                        FontAndColorBottomSheet(
                            selectedFont = fontStyle,
                            selectedColor = textColor,
                            onFontSelected = {
                                fontStyle = it
                            },
                            onColorSelected = { textColor = it }
                        )
                    }

                }

                }

            }


        }

    }












