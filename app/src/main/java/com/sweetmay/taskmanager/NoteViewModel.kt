package com.sweetmay.taskmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import java.util.*

class NoteViewModel(app:Application) : AndroidViewModel(app) {

    private var pendingNote: Note? = null

    private val emptyTitle = getApplication<Application>().resources.getString(R.string.empty_note_title)
    private val emptyBody = getApplication<Application>().resources.getString(R.string.empty_note_body)

    fun setPendingNote(note: Note){
        pendingNote = note
    }

    fun save(title: String, body: String){

        if(title.isEmpty() && body.isEmpty()){
            return
        }

        val checkedTitle = if(title.isEmpty()) emptyTitle else title
        val checkedBody = if(body.isEmpty()) emptyBody else body

        pendingNote?.let {
            it.title = checkedTitle
            it.text = checkedBody
            it.date = Date()
            Repository.editOrAddNote(it)
            return
        }
        Repository.editOrAddNote(Note(checkedTitle, checkedBody, Date(), UUID.randomUUID().toString()))
    }

    override fun onCleared() {
        super.onCleared()
    }
}