package com.example.gdutnews.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.gdutnews.logic.Repository

class CategoryViewModel : ViewModel() {

    private val getItemLiveData = MutableLiveData<String>()

    val itemLiveData = Transformations.switchMap(getItemLiveData) { category ->
        Repository.getNextPage(category)
    }

    fun getItems(category: String) {
        getItemLiveData.value = category
    }

}