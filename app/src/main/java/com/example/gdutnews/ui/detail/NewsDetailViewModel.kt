package com.example.gdutnews.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.gdutnews.logic.Repository

class NewsDetailViewModel : ViewModel() {

    private val idLiveData = MutableLiveData<String>()

    val content = Transformations.switchMap(idLiveData) { idLiveData ->
        Repository.getPage(idLiveData)
    }

    fun getPage(id: String){
        idLiveData.value = id
    }

}