package com.example.gdutnews.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.gdutnews.logic.Repository

class SearchViewModel : ViewModel() {

    private val infoLiveData = MutableLiveData<Any?>()

    var url = ""

    // 默认ID值
    var typeID = "title"
    var departmentID = 2147483647
    var categoryID = 2147483647

    val result = Transformations.switchMap(infoLiveData){ infoLiveData ->
        Repository.getNormalPage(url)
    }

    fun getResult(){
        infoLiveData.value = infoLiveData.value
    }
}