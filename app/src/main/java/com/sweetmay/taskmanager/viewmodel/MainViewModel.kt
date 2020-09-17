package com.sweetmay.taskmanager.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.sweetmay.taskmanager.model.Note
import com.sweetmay.taskmanager.model.NoteResult
import com.sweetmay.taskmanager.model.Repository


class MainViewModel : ViewModel(){
    private var notes = MutableLiveData<List<Note>>()
    private val notesObserver = Observer<NoteResult> { result->
        result ?: return@Observer
        when(result){
            is NoteResult.Success<*> -> notes.value = result.data as? List<Note>
            is NoteResult.Error -> Log.d("a", result.toString())
        }
    }

    init {
        Repository.getNotes().observeForever(notesObserver)
    }


    fun getNotes(): LiveData<List<Note>> = notes

}