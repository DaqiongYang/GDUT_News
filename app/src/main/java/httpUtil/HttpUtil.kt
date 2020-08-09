package httpUtil

import android.util.Log
import okhttp3.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException

object HttpUtil {
    fun initSessionId(listener: CallBackListener){
        var sessionID = ""
        var data1: String? = ""
        var headers: Headers
        var cookies: List<String>
        httpRequest(address = "http://news.gdut.edu.cn/", callback =  object : Callback {
            override fun onResponse(call: Call, response: Response) {
                data1 = response.body?.string()
                // 获取Cookie
                headers = response.headers
                cookies = headers.values("Set-Cookie")
                val session = cookies[0]
                sessionID = session.substring(0, session.indexOf(";"))
                // 获取隐藏域
                val parse: Document = Jsoup.parse(data1)
                val selects: Elements = parse.select("input[type=hidden]")
                val element1: Element = selects[0]
                val __VIEWSTATE: String = element1.attr("value")
                val element2: Element = selects[1]
                val __EVENTVALIDATION: String = element2.attr("value")
                // 模拟登录
                val formBody = FormBody.Builder()
                    .add("__VIEWSTATE", __VIEWSTATE)
                    .add("__EVENTVALIDATION", __EVENTVALIDATION)
                    .add("ctl00\$ContentPlaceHolder1\$userEmail", "gdutnews")
                    .add("ctl00\$ContentPlaceHolder1\$userPassWord", "newsgdut")
                    .add("ctl00\$ContentPlaceHolder1\$CheckBox1", "on")
                    .add("ctl00\$ContentPlaceHolder1\$Button1", "登录")
                    .build()
                httpRequestLogin(formBody, sessionID, object : Callback{
                    override fun onResponse(call: Call, response: Response) {
                        val data = response.body?.string()
                        // 回调 onFinish() 方法
                        listener.onFinish(sessionID)
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        // 回调 onError() 方法
                        listener.onError(e)
                    }
                })
            }

            override fun onFailure(call: Call, e: IOException) {
                listener.onError(e)
            }
        })
    }

    fun httpRequest(address: String, sessionID: String = "", callback: Callback) {
        val client = OkHttpClient()
        val request : Request
        if (sessionID == ""){
            request = Request.Builder()
                .url(address)
                .build()
        }else{
            request = Request.Builder()
                .url(address)
                .header("Cookie", sessionID)
                .build()
        }
        client.newCall(request).enqueue(callback)
    }

    fun httpRequestLogin(formBody: FormBody, sessionID: String, callback: Callback) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://news.gdut.edu.cn/UserLogin.aspx?preURL=http://news.gdut.edu.cn/default.aspx")
            .post(formBody)
            .header("Cookie", sessionID)
            .build()
        client.newCall(request).enqueue(callback)
    }

    fun httpGotoNextPage(url: String, formBody: FormBody, sessionID: String, callback: Callback){
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .header("Cookie", sessionID)
            .build()
        client.newCall(request).enqueue(callback)
    }

}