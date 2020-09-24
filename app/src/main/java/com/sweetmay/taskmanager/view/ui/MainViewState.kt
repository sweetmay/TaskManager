package com.sweetmay.taskmanager.view.ui

import com.sweetmay.taskmanager.model.Note
import com.sweetmay.taskmanager.view.ui.base.BaseViewState

class MainViewState(val notes: List<Note>? = null, error: Throwable? = null) : BaseViewState<List<Note>?>(notes, error)