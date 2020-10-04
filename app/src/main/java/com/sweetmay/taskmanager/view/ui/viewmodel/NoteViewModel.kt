package com.sweetmay.taskmanager.view.ui.viewmodel


import com.sweetmay.taskmanager.R
import com.sweetmay.taskmanager.model.Note
import com.sweetmay.taskmanager.model.Repository
import com.sweetmay.taskmanager.view.ui.App
import com.sweetmay.taskmanager.view.ui.NoteData
import com.sweetmay.taskmanager.view.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.util.*

class NoteViewModel(val repository: Repository) : BaseViewModel<NoteData>() {

    private var pendingNote: Note? = null
    private var toDelete: Boolean = false

    private val emptyTitle
        get() = App.instance.resources.getString(R.string.empty_note_title)
    private val emptyBody
        get() = App.instance.resources.getString(R.string.empty_note_body)


    fun loadNote(id: String) = launch{
        try {
            repository.getNoteById(id)?.let {
                setData(NoteData(note = it))
                pendingNote = it
            }
        }catch (e: Throwable){
            setError(e)
        }
    }


    fun save(title: String, body: String, color: Int){
        if(!toDelete){
        if(title.isEmpty() && body.isEmpty()){
            return
        }
        val checkedTitle = if(title.isEmpty()) emptyTitle else title
        val checkedBody = if(body.isEmpty()) emptyBody else body

        pendingNote?.let {
            if(it.title != checkedTitle || it.text != checkedBody){
                it.title = checkedTitle
                it.text = checkedBody
                it.date = Date()
                it.color = color
            }else {
                it.color = color
                it.title = checkedTitle
                it.text = checkedBody
            }
        } ?: createNewNote(checkedTitle, checkedBody, color )
            launch {
                pendingNote?.let { repository.saveNote(it) }
            }
    }
    }

    private fun createNewNote(checkedTitle: String, checkedBody: String, color: Int) {
        pendingNote = Note(checkedTitle, checkedBody, Date(), UUID.randomUUID().toString(), color)
    }


    fun deleteNote() = launch{
        try {
            toDelete = true
            pendingNote?.id?.let { repository.deleteNoteById(it) }
        }catch (e: Throwable){
            setError(e)
        }
    }
}