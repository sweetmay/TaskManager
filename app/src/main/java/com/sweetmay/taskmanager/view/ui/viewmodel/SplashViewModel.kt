package com.sweetmay.taskmanager.view.ui.viewmodel

import androidx.lifecycle.Observer
import com.sweetmay.taskmanager.model.Repository
import com.sweetmay.taskmanager.model.User
import com.sweetmay.taskmanager.model.errors.NoAuthException
import com.sweetmay.taskmanager.view.ui.SplashViewState
import com.sweetmay.taskmanager.view.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SplashViewModel(val repository: Repository): BaseViewModel<Boolean?>() {

    fun requestUser() = launch{
        repository.getCurrentUser()?.let {
            setData(true)
        } ?: setError(NoAuthException())
    }

}