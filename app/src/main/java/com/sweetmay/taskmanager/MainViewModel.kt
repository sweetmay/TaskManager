package com.sweetmay.taskmanager

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel


class MainViewModel : ViewModel(), OnNotesChanged {
    private var notes = MutableLiveData<ArrayList<Note>>()

    init {
        notes.value = Repository.getNotes()
    }

    fun addNote(note: Note){
        Repository.addNote(note, this)
    }

    fun getNotes(): MutableLiveData<ArrayList<Note>> = notes

    override fun updateData(notes: ArrayList<Note>) {
        this.notes.value = notes
    }
}