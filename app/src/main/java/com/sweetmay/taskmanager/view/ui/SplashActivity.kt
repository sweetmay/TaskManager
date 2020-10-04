package com.sweetmay.taskmanager.view.ui

import com.sweetmay.taskmanager.view.ui.base.BaseActivity
import com.sweetmay.taskmanager.view.ui.viewmodel.SplashViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity: BaseActivity<Boolean?>() {

    override val viewModel: SplashViewModel by viewModel()

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