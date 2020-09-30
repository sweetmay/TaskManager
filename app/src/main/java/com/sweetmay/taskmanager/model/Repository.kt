package com.sweetmay.taskmanager.model

class Repository(val dataProvider: DataProvider) {

    fun getCurrentUser() = dataProvider.getCurrentUser()
    fun getNotes() = dataProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = dataProvider.saveNote(note)
    fun getNoteById(id: String) = dataProvider.getById(id)
    fun deleteNoteById(id: String) = dataProvider.deleteNoteById(id)
}