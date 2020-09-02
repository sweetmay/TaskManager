package com.sweetmay.taskmanager

class Model {

    private var clicks: Int = 0

    fun getData(OnRequestData: OnRequestData){
        return OnRequestData.requestData(clicks)
    }

    fun incrementData(): Int? {
        clicks++
        return clicks;
    }

}