package com.example.gdutnews.logic.network

import android.util.Log
import okhttp3.Call
import okhttp3.FormBody
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object GDUTNewsNetwork {

    suspend fun request(address: String, session: String? = null, fromBody: FormBody? = null): Response{
        return suspendCoroutine { continuation ->
            HttpRequest.sendHttpRequest(address, session, fromBody, object : okhttp3.Callback{
                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(response)
                }

                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }
            })
        }
    }

    suspend fun requestReturnString(address: String, session: String? = null, fromBody: FormBody? = null): String?{
        return suspendCoroutine { continuation ->
            HttpRequest.sendHttpRequest(address, session, fromBody, object : okhttp3.Callback{
                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(response.body?.string())
                }

                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }
            })
        }
    }
}