@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.diarywithlock.notification

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.diarywithlock.notification.database.ReminderDatabaseInstance
import com.example.diarywithlock.notification.database.ReminderEntity
import com.example.diarywithlock.notification.repository.ReminderRepository
import com.example.diarywithlock.notification.utils.convertToMillis
import com.example.diarywithlock.notification.viewmodel.ReminderViewModel
import com.example.diarywithlock.notification.viewmodel.ReminderViewModelFactory
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone
import androidx.compose.foundation.lazy.items
@Composable
fun ReminderScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    val database = ReminderDatabaseInstance.getDatabase(context)
    val repository = ReminderRepository(database.reminderDao())
    val factory = remember { ReminderViewModelFactory(context, repository) }
    val viewModel: ReminderViewModel = viewModel(factory = factory)


    val isEditing = remember { mutableStateOf(false) }
    val showTimePicker = remember { mutableStateOf(false) }
    var reminderBeingEdited by remember { mutableStateOf<ReminderEntity?>(null) }


    val reminders by viewModel.reminders.collectAsState()

    if (showTimePicker.value || reminderBeingEdited != null) {
        ReminderTimePickerDialog(
            initialTime = reminderBeingEdited?.time,
            isEdit = isEditing.value,
            onDismiss = {
                showTimePicker.value = false
                isEditing.value = false
                reminderBeingEdited = null
            },
            onConfirm = { formattedTime ->
                if (isEditing.value && reminderBeingEdited != null) {
                    val oldReminder = reminderBeingEdited!!
                    val updated = oldReminder.copy(
                        time = formattedTime,
                        timestampMillis = convertToMillis(formattedTime)
                    )
                    viewModel.updateReminder(updated)
                } else {
                    viewModel.addReminder(formattedTime)
                }

                showTimePicker.value = false
                isEditing.value = false
                reminderBeingEdited = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reminder", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1F61A8)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showTimePicker.value = true },
                containerColor = Color(0xFF1F61A8)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Reminder",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        containerColor = Color(0xFFF8FAFC)
    ) { innerPadding ->
        if (reminders.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Alarm,
                        contentDescription = "No Alarms",
                        modifier = Modifier.size(48.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No Alarms Set",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding() + 16.dp,
                    bottom = innerPadding.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = reminders,
                ) { reminder ->
                    ReminderItemCard(
                        reminder = reminder,
                        onToggle = { viewModel.toggleReminder(reminder) },
                        onEdit = {
                            isEditing.value = true
                            reminderBeingEdited = reminder
                            showTimePicker.value = true
                        },
                        onDelete = { viewModel.deleteReminder(reminder) }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

        }
    }
}

@Composable
fun ReminderItemCard(
    reminder: ReminderEntity,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier
            .width(320.dp)
            .height(72.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(modifier = Modifier.width(75.dp)) {
                Text(
                    text = reminder.time,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000),
                    maxLines = 1
                )
            }


            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onToggle) {
                    Icon(
                        imageVector = if (reminder.isEnabled) Icons.Filled.ToggleOn else Icons.Filled.ToggleOff,
                        contentDescription = if (reminder.isEnabled) "Turn Off" else "Turn On",
                        tint = if (reminder.isEnabled) Color(0xFF1F61A8) else Color.Gray,
                        modifier = Modifier.size(44.dp)
                    )
                }

                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .padding(8.dp)
                        .height(25.dp),
                    border = BorderStroke(0.1.dp, Color(0xFF1F61A8)),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFF1F61A8),
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = Color(0xFFFFFFFF),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Edit",
                        color = Color(0XFFFFFFFF),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .padding(4.dp)
                        .wrapContentWidth()
                        .height(25.dp),
                    border = BorderStroke(0.1.dp, Color(0xFF1F61A8)),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFFD36E15),
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFFFFFFF),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Delete",
                        color = Color(0XFFFFFFFF),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}


@Composable
fun ReminderTimePickerDialog(
    initialTime: String? = null,
    isEdit: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {

    val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Karachi"))
    val hour24 = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val amPm = if (hour24 < 12) "AM" else "PM"
    val hour12 = when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
    }

    val timeParts = initialTime?.split(":", " ")
    val defaultHour = timeParts?.getOrNull(0) ?: hour12.toString()
    val defaultMinute = timeParts?.getOrNull(1) ?: String.format("%02d", minute)
    val defaultAmPm = timeParts?.getOrNull(2) ?: amPm



    var selectedHour by remember { mutableStateOf(defaultHour) }
    var selectedMinute by remember { mutableStateOf(defaultMinute) }
    var selectedAmPm by remember { mutableStateOf(defaultAmPm) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(320.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TimeWheelPicker(
                        label = "Hour",
                        values = (1..12).map { it.toString() },
                        selectedValue = selectedHour,
                        onValueChange = { selectedHour = it }
                    )

                    TimeWheelPicker(
                        label = "Minute",
                        values = (0..59).map { String.format("%02d", it) },
                        selectedValue = selectedMinute,
                        onValueChange = { selectedMinute = it }
                    )

                    TimeWheelPicker(
                        label = "AM/PM",
                        values = listOf("AM", "PM"),
                        selectedValue = selectedAmPm,
                        onValueChange = { selectedAmPm = it }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val time = "$selectedHour:$selectedMinute $selectedAmPm"
                        onConfirm(time)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F61A8))
                ) {
                    Text(
                        text = if (isEdit) "Update" else "Save",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )

                }
            }
        }
    }
}

@Composable
fun TimeWheelPicker(
    label: String,
    values: List<String>,
    selectedValue: String,
    onValueChange: (String) -> Unit
) {
    val itemHeight = 44.dp
    val pickerHeight = itemHeight * 3
    val pickerWidth = 71.dp

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val loopList = if (values.size > 2) {

        List(1000) { index -> values[index % values.size] }
    } else {

        values
    }


    val itemHeightPx = with(LocalDensity.current) { itemHeight.toPx() }

    LaunchedEffect(Unit) {
        val index = if (values.size > 2) {
            500 + values.indexOf(selectedValue)
        } else {
            values.indexOf(selectedValue)
        }
        listState.scrollToItem(index)
    }


    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val offset = listState.firstVisibleItemScrollOffset
            val baseIndex = listState.firstVisibleItemIndex
            val centerIndex = if (offset >= itemHeightPx / 2f) baseIndex + 1 else baseIndex

            coroutineScope.launch {
                listState.animateScrollToItem(centerIndex)
            }

            loopList.getOrNull(centerIndex)?.let {
                onValueChange(it)
            }
        }
    }

    val centerIndex by remember {
        derivedStateOf {
            val offset = listState.firstVisibleItemScrollOffset
            val base = listState.firstVisibleItemIndex
            if (offset >= itemHeightPx / 2f) base + 1 else base
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(3.dp))
        Box(
            modifier = Modifier
                .height(pickerHeight)
                .width(pickerWidth)
                .border(1.dp, Color(0XFF1F61A8), RoundedCornerShape(8.dp))
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .height(itemHeight)
                    .fillMaxWidth()
                    .background(Color(0x221976D2))
            )

            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(top = itemHeight + 4.dp, bottom = itemHeight),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(loopList) { index, item ->
                    val isCenter = index == centerIndex
                    Box(
                        modifier = Modifier
                            .height(itemHeight)
                            .fillMaxWidth()
                            .clickable {
                                coroutineScope.launch {
                                    listState.animateScrollToItem(index)
                                    onValueChange(item)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item,
                            fontSize = if (isCenter) 16.sp else 14.sp,
                            fontWeight = if (isCenter) FontWeight.Bold else FontWeight.SemiBold,
                            color = if (isCenter) Color(0xFF1976D2) else Color.Black,
                            modifier = Modifier.alpha(if (isCenter) 1f else 0.3f)
                        )
                    }
                }
            }
        }
    }
}
