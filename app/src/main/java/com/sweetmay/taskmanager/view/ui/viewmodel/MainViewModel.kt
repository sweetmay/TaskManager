package com.sweetmay.taskmanager.view.ui.viewmodel

import androidx.lifecycle.Observer
import com.sweetmay.taskmanager.model.Note
import com.sweetmay.taskmanager.model.NoteResult
import com.sweetmay.taskmanager.model.Repository
import com.sweetmay.taskmanager.view.ui.MainViewState
import com.sweetmay.taskmanager.view.ui.base.BaseViewModel

class MainViewModel(): BaseViewModel<List<Note>?, MainViewState>(){

    private val notesObserver = Observer<NoteResult> {
        it ?: return@Observer
        when(it){
            is NoteResult.Success<*> -> viewStateLiveData.value = MainViewState(notes = it.data as? List<Note>)
            is NoteResult.Error -> viewStateLiveData.value = MainViewState(error = it.error)
        }
    }

    private val repositoryNotes = Repository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever (notesObserver)
    }

    override fun onCleared() {
        super.onCleared()
        repositoryNotes.removeObserver(notesObserver)
    }
}