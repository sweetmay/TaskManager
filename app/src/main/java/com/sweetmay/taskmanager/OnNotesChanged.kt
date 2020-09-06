package com.sweetmay.taskmanager

interface OnNotesChanged {
    fun updateData(notes: ArrayList<Note>)
}