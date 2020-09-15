package com.sweetmay.taskmanager

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel


class MainViewModel : ViewModel(){
    private var notes = MutableLiveData<ArrayList<Note>>()

    init {
        Repository.getNotes().observeForever { arr->
            notes.value = arr
        }
    }

    fun getNotes(): LiveData<ArrayList<Note>> = notes

}