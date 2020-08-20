package com.example.gdutnews.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.gdutnews.logic.Repository
import com.example.gdutnews.logic.model.NewsInfo
import com.example.gdutnews.util.HiddenInput
import okhttp3.FormBody

class NewsListViewModel : ViewModel() {

    var categoryID = ""

    var address = ""

    var info: Map<String, Any>? = null

    var hiddenInput: HiddenInput? = null

    var currentPage = 1

    var allPage = 0

    var newPosition = 0

    val newsList = ArrayList<NewsInfo>()

    private val getPageLiveData = MutableLiveData<FormBody>()

    private val searchLiveData = MutableLiveData<FormBody>()

    val result = Transformations.switchMap(getPageLiveData) {
        Repository.getCategoryPage(categoryID, getPageLiveData.value)
    }

    val searchResult = Transformations.switchMap(searchLiveData) {
        Repository.getCategoryPage(categoryID, searchLiveData.value, address)
    }

    fun getPage(formBody: FormBody) {
        getPageLiveData.value = formBody
    }

    fun getSearchPage(formBody: FormBody) {
        searchLiveData.value = formBody
    }
}