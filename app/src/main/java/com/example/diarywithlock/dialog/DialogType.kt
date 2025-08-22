package com.example.diarywithlock.dialog

sealed class SelectionBottomSheet {
    object Mood : SelectionBottomSheet()
    object Font : SelectionBottomSheet()
    object Background : SelectionBottomSheet()
    object DatePicker : SelectionBottomSheet()
    object Emoji : SelectionBottomSheet()
    object HashTag : SelectionBottomSheet()
    object AudioRecording : SelectionBottomSheet()
}