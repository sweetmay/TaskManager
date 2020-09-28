package com.sweetmay.taskmanager.view.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.sweetmay.taskmanager.model.errors.NoAuthException

abstract class BaseActivity<T, S: BaseViewState<T>>: AppCompatActivity() {
    
    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int?

    companion object{
        private const val RC_SIGN_IN = 1005
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRes?.let {
            setContentView(it)
        }
        viewModel.getViewState().observe(this, Observer {
            it ?: return@Observer
            it.error?.let {
                renderError(it)
                return@Observer
            }
            renderData(it.data)
        })
    }

    abstract fun renderData(data: T)

    protected open fun renderError(error: Throwable){
        when(error){
            is NoAuthException -> startLogin()
            else -> error.message?.let { showError(it) }
        }
    }

    fun startLogin(){
        val providers = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK){
            finish()
        }
    }

    fun showError(errorMsg: String){
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
    }
}