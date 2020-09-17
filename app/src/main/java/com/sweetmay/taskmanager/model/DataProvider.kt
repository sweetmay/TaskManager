package com.sweetmay.taskmanager.model

import androidx.lifecycle.LiveData
import com.sweetmay.taskmanager.model.Note
import com.sweetmay.taskmanager.model.NoteResult

interface DataProvider {
    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
    fun getById(id: String): LiveData<NoteResult>
}