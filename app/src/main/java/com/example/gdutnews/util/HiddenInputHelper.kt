package com.example.gdutnews.util

import org.jsoup.Jsoup

object HiddenInputHelper {
    // 获取隐藏域
    fun getHiddenInfo(data: String): HiddenInput{
        val doc = Jsoup.parse(data)
        val selects = doc.select("input[type=hidden]")
        val element1 = selects[0]
        val __VIEWSTATE: String = element1.attr("value")
        val element2 = selects[1]
        val __EVENTVALIDATION: String = element2.attr("value")
        return HiddenInput(__VIEWSTATE, __EVENTVALIDATION)
    }

}