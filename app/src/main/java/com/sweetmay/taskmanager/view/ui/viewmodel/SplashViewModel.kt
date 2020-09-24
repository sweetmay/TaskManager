package com.sweetmay.taskmanager.view.ui.viewmodel

import android.util.Log
import androidx.lifecycle.Observer
import com.sweetmay.taskmanager.model.NoteResult
import com.sweetmay.taskmanager.model.Repository
import com.sweetmay.taskmanager.model.User
import com.sweetmay.taskmanager.model.errors.NoAuthException
import com.sweetmay.taskmanager.view.ui.SplashViewState
import com.sweetmay.taskmanager.view.ui.base.BaseViewModel

class SplashViewModel(): BaseViewModel<Boolean?, SplashViewState>() {

    private val user
        get() = Repository.getCurrentUser()

    private val notesObserver = Observer<User?>{
        viewStateLiveData.value = if(it!=null){
            SplashViewState(isAuthenticated = true)
        }else {
            SplashViewState(error = NoAuthException())
        }
    }

    fun requestUser(){
        user.observeForever(notesObserver)
    }

    override fun onCleared() {
        super.onCleared()
        user.removeObserver(notesObserver)
    }

}