package com.example.gdutnews

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_news_detail.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.jsoup.Jsoup
import java.io.IOException


class NewsDetailActivity : AppCompatActivity() {

    private var url : String? = null

    @SuppressLint("SetJavaScriptEnabled", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.setDownloadListener { url, _, _, _, _ ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        // 设置背景颜色为透明，否则会闪白屏
        webView.setBackgroundColor(android.R.color.transparent)
        webView.settings.setSupportZoom(true)

        url = "http://news.gdut.edu.cn/ViewArticle.aspx?articleid=${intent.getStringExtra("id")}"
        url?.let {
            httpUtil.HttpUtil.httpRequest(it, Session.SessionID, object : Callback{
                @SuppressLint("SetJavaScriptEnabled")
                override fun onResponse(call: Call, response: Response) {
                    val rawContent = response.body?.string()
                    var title = ""
                    val doc = Jsoup.parse(rawContent)
                    val element = doc.select("div[class=articleBody]")
                    if (!element[0].select("span").isEmpty()){
                        // 较新文章的标题样式
                        title = element[0].select("span")[0].text()
                    }else{
                        // 较旧文章的标题样式
                        title = element[0].select("h1")[0].text()
                    }
                    val content = element[0].toString()
                    val varjs =
                        "<script type='text/javascript'> \nwindow.onload = function()\n{var \$img = document.getElementsByTagName('img');for(var p in  \$img){\$img[p].style.width = '100%'; \$img[p].style.height ='auto'}}</script>"
                    val setColor = if ((this@NewsDetailActivity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES){
                        "<style type=\"text/css\">\n" +
                                "body {background-color: #2E2E2E; color: #BBBBBB}\n" +
                                "p {background-color: #2E2E2E; color: #BBBBBB}" +
                                "</style>"
                    }else{
                        ""
                    }
                    runOnUiThread {
                        supportActionBar?.title = title
                        webView.loadDataWithBaseURL("http://news.gdut.edu.cn/", content + varjs + setColor, "text/html", "UTF-8", null)
                        webView.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
            })
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack()
        }else{
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.open_in_browser -> {
                val intentNew = Intent(Intent.ACTION_VIEW)
                intentNew.data = Uri.parse(url)
                startActivity(intentNew)
            }
            R.id.copyToClipboard -> {
                // 复制到剪贴板
                val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val cbData = ClipData.newPlainText("url", url)
                cm.setPrimaryClip(cbData)
                Toast.makeText(this, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
            }
            android.R.id.home ->  {
                finish()
            }
        }
        return true
    }

}