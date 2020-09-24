package com.sweetmay.taskmanager.view.ui

import com.sweetmay.taskmanager.model.Note
import com.sweetmay.taskmanager.view.ui.base.BaseViewState

class NoteViewState(val note: Note? = null, error: Throwable? = null): BaseViewState<Note?>(note, error) {
}