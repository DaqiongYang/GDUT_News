package httpUtil

import java.lang.Exception

interface CallBackListener {
    fun onFinish(session : String)
    fun onError(e : Exception)
}