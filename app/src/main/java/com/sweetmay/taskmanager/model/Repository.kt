package com.sweetmay.taskmanager.model

object Repository {

    private val dataProvider: DataProvider = FireStoreProvider()
    fun getNotes() = dataProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = dataProvider.saveNote(note)
    fun getNoteById(id: String) = dataProvider.getById(id)

}