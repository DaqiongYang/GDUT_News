package com.example.gdutnews.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.gdutnews.R
import com.example.gdutnews.util.Clipboard
import kotlinx.android.synthetic.main.activity_news_detail.*
import org.jsoup.Jsoup


class NewsDetailActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(NewsDetailViewModel::class.java) }

    @SuppressLint("SetJavaScriptEnabled", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.setDownloadListener { url, _, _, _, _ ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        // 设置背景颜色为透明，否则深色模式下会闪白屏
        webView.setBackgroundColor(android.R.color.transparent)


        val id = intent.getStringExtra("id")
        viewModel.content.observe(this) { result ->
            val rawContent = result.getOrNull()
            if (rawContent != null) {
                loadArticle(rawContent)
            }
        }

        viewModel.getPage(id)

    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.open_in_browser -> {
                val intentNew = Intent(Intent.ACTION_VIEW)
                intentNew.data = Uri.parse(
                    "http://news.gdut.edu.cn/ViewArticle.aspx?articleid=${intent.getStringExtra(
                        "id"
                    )}"
                )
                startActivity(intentNew)
            }
            R.id.copyToClipboard -> {
                Clipboard.copyToClipboard(
                    "http://news.gdut.edu.cn/ViewArticle.aspx?articleid=${intent.getStringExtra(
                        "id"
                    )}"
                )
            }
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }

    private fun loadArticle(rawContent: String) {
        var title = ""
        val doc = Jsoup.parse(rawContent)
        val element = doc.select("div[class=articleBody]")
        if (!element[0].select("span").isEmpty()) {
            // 较新文章的标题样式
            title = element[0].select("span")[0].text()
        } else {
            // 较旧文章的标题样式
            title = element[0].select("h1")[0].text()
        }
        val content = element[0].toString()
        // 图片自适应尺寸
        val varjs =
            "<script type='text/javascript'> \nwindow.onload = function()\n{var \$img = document.getElementsByTagName('img');for(var p in  \$img){\$img[p].style.width = '100%'; \$img[p].style.height ='auto'}}</script>"
        // 深色模式下设置颜色
        val setColor =
            if ((this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
                "<style type=\"text/css\">\n" +
                        "body {background-color: #141414; color: #BBBBBB}\n" +
                        "p {background-color: #141414; color: #BBBBBB}" +
                        "</style>"
            } else {
                ""
            }
        supportActionBar?.title = title
        webView.loadDataWithBaseURL(
            "http://news.gdut.edu.cn/",
            content + varjs + setColor,
            "text/html",
            "UTF-8",
            null
        )
        webView.visibility = View.VISIBLE
    }

}