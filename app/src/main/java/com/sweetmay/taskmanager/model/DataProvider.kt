package com.sweetmay.taskmanager.model

import androidx.lifecycle.LiveData
import com.sweetmay.taskmanager.model.Note
import com.sweetmay.taskmanager.model.NoteResult
import kotlinx.coroutines.channels.ReceiveChannel

interface DataProvider {
    fun subscribeToAllNotes(): ReceiveChannel<NoteResult>
    suspend fun getCurrentUser(): User?
    suspend fun saveNote(note: Note): Note
    suspend fun getById(id: String): Note?
    suspend fun deleteNoteById(id: String)
}