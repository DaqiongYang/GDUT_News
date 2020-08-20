package com.example.gdutnews.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.gdutnews.logic.Repository

class MainViewModel : ViewModel() {

    var selectedItem = 0

    var title = ""

    private val getItemLiveData = MutableLiveData<String>()

    val itemLiveData = Transformations.switchMap(getItemLiveData) { category ->
        Repository.getCategoryPage(category)
    }

    fun getItems(category: String) {
        getItemLiveData.value = category
    }


}