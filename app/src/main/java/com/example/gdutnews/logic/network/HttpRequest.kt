package com.example.gdutnews.logic.network

import android.util.Log
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread

object HttpRequest {

    fun sendHttpRequest(address: String, session: String? = null, fromBody: FormBody? = null, callback: Callback){
            val client = OkHttpClient()
            val requestBuilder = Request.Builder().url(address)
            if (session != null){
                requestBuilder.addHeader("Cookie", session)
            }
            if (fromBody != null){
                requestBuilder.post(fromBody)
            }
            val request = requestBuilder.build()
            client.newCall(request).enqueue(callback)
    }

}