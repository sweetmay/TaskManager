package com.sweetmay.taskmanager.view.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.sweetmay.taskmanager.R
import com.sweetmay.taskmanager.model.Note
import com.sweetmay.taskmanager.model.NoteResult
import com.sweetmay.taskmanager.model.Repository
import com.sweetmay.taskmanager.view.ui.App
import com.sweetmay.taskmanager.view.ui.NoteViewState
import com.sweetmay.taskmanager.view.ui.base.BaseViewModel
import java.util.*

class NoteViewModel(val repository: Repository) : BaseViewModel<Note?, NoteViewState>() {

    private var repositoryNotes: LiveData<NoteResult> = repository.getNoteById("")
    private val emptyTitle = App.instance.resources.getString(R.string.empty_note_title)
    private val emptyBody = App.instance.resources.getString(R.string.empty_note_body)
    private var pendingNote: Note? = null
    private var toDelete: Boolean = false

    private val notesObserver = Observer<NoteResult> {
        it ?: return@Observer
        when(it){
            is NoteResult.Success<*> -> {
                viewStateLiveData.value = NoteViewState(note = it.data as Note?)
                pendingNote = it.data as Note
            }
            is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = it.error)
        }
    }

    fun loadNote(id: String){
        repositoryNotes = repository.getNoteById(id)
        repositoryNotes.observeForever (notesObserver)
    }

    init {
        viewStateLiveData.value = NoteViewState()
    }


    fun save(title: String, body: String){
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
                }else {
                    it.title = checkedTitle
                    it.text = checkedBody
                }
                repository.saveNote(it)
            } ?: repository.saveNote(Note(checkedTitle, checkedBody, Date(), UUID.randomUUID().toString()))

        }
    }

    fun deleteNote(){
        toDelete = true
        pendingNote?.id?.let { repository.deleteNoteById(it) }
    }

    override fun onCleared() {
        super.onCleared()
        repositoryNotes.removeObserver(notesObserver)
    }
}