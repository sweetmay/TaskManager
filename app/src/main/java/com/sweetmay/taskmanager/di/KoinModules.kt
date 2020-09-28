package com.sweetmay.taskmanager.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sweetmay.taskmanager.model.DataProvider
import com.sweetmay.taskmanager.model.FireStoreProvider
import com.sweetmay.taskmanager.model.Repository
import com.sweetmay.taskmanager.view.ui.viewmodel.MainViewModel
import com.sweetmay.taskmanager.view.ui.viewmodel.NoteViewModel
import com.sweetmay.taskmanager.view.ui.viewmodel.SplashViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single<DataProvider> { FireStoreProvider(get(), get()) }
    single { Repository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}

val mainModule = module {
    viewModel {MainViewModel(get())}
}