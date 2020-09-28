package com.sweetmay.taskmanager.view.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.sweetmay.taskmanager.view.ui.base.BaseActivity
import com.sweetmay.taskmanager.view.ui.viewmodel.SplashViewModel

class SplashActivity: BaseActivity<Boolean?, SplashViewState>() {

    override val viewModel by lazy{
        ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestUser()
    }

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let {
            startMainActivity()
        }
    }

    fun startMainActivity(){
        MainActivity.start(this)
        finish()
    }

    override val layoutRes: Int? = null
}