package com.sweetmay.taskmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*
import kotlin.collections.ArrayList

object Repository {

    private var notes = MutableLiveData<ArrayList<Note>>()
    private var noteList: ArrayList<Note> = arrayListOf()

    init {
        notes.value = noteList
    }
    fun getNotes(): MutableLiveData<ArrayList<Note>> {
        return notes
    }

    private fun saveNote(){
        notes.value = noteList
    }

    private fun add(note: Note){
        noteList.add(note)
        saveNote()
    }

    fun editOrAddNote(note: Note){
        for (i in 0 until noteList.size){
            if (noteList[i].id == note.id){
                noteList[i] = note
                saveNote()
                return
            }
        }
        add(note)
    }

}