package com.example.gdutnews.ui.misc

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.gdutnews.logic.Repository

class SplashViewModel : ViewModel() {

    private val getSessionIDLiveData = MutableLiveData<Any?>()

    val sessionIDLiveData = Transformations.switchMap(getSessionIDLiveData){ input ->
        Repository.initSession()
    }

    fun getSessionID(){
        getSessionIDLiveData.value = getSessionIDLiveData.value
    }

}