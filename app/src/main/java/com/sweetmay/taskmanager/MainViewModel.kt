package com.sweetmay.taskmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MainViewModel : ViewModel() {
    private var model: Model = Model()
    private var clicks = MutableLiveData<Int>()

    fun update(){
        model.incrementData()
        model.getData(object : OnRequestData{
            override fun requestData(data: Int){
                clicks.value = data
            }
        })
    }

    fun getViewState(): LiveData<Int> = clicks
}