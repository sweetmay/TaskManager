package com.sweetmay.taskmanager.model

class Repository(val dataProvider: DataProvider) {

    suspend fun getCurrentUser() = dataProvider.getCurrentUser()
    fun getNotes() = dataProvider.subscribeToAllNotes()
    suspend fun saveNote(note: Note) = dataProvider.saveNote(note)
    suspend fun getNoteById(id: String) = dataProvider.getById(id)
    suspend fun deleteNoteById(id: String) = dataProvider.deleteNoteById(id)
}