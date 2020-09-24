package com.sweetmay.taskmanager.view.callback

import com.sweetmay.taskmanager.model.Note

interface OnItemRVClick {
    fun onItemClick(note: Note)
}