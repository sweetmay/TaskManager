package com.sweetmay.taskmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*
import kotlin.collections.ArrayList

object Repository {
    private var notes = ArrayList<Note>()

    init {
        notes = arrayListOf()
    }

    fun getNotes(): ArrayList<Note> {
        return notes
    }

    fun addNote(note: Note, onNotesChanged: OnNotesChanged){
        notes.add(note)
        onNotesChanged.updateData(notes)
    }
}