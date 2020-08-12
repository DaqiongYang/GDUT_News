package com.example.gdutnews.ui.main

import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.gdutnews.*
import com.example.gdutnews.ui.detail.NewsDetailActivity
import com.example.gdutnews.ui.misc.GotoActivity
import com.example.gdutnews.ui.list.CategoryActivity
import com.example.gdutnews.ui.search.SearchActivity
import com.example.gdutnews.util.Clipboard
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        xntz.setOnClickListener {
            getInfo(4)
        }
        gsgg.setOnClickListener {
            getInfo(5)
        }
        xnjx.setOnClickListener {
            getInfo(6)
        }
        zbgg.setOnClickListener {
            getInfo(8)
        }
        searchButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        gotoButton.setOnClickListener {
            val intent = Intent(this, GotoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getInfo(category: Int) {
        if (GDUTNewsApplication.session != "") {
            val intent = Intent(this@MainActivity, CategoryActivity::class.java)
            intent.putExtra("categoryID", category)
            startActivity(intent)
            GDUTNewsApplication.notToOpen = true
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        // 在获取焦点时读取剪贴板文字
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus and !GDUTNewsApplication.notToOpen) {
            showOpenDialog()
        }
    }

    private fun showOpenDialog() {
        // 读取剪贴板的逻辑
        val clipText = Clipboard.getClipboard()
        if (clipText.startsWith("http://news.gdut.edu.cn/viewarticle.aspx?articleid=") or
            clipText.startsWith("news.gdut.edu.cn/viewarticle.aspx?articleid=") or
            clipText.startsWith("https://news.gdut.edu.cn/viewarticle.aspx?articleid=")
        ) {
            AlertDialog.Builder(this@MainActivity).apply {
                Log.d("debug", clipText)
                setTitle("是否跳转？")
                setMessage("检测到剪贴板里有通知网链接，是否打开")
                setPositiveButton("确定") { dialog, which ->
                    var id: String? = null
                    when {
                        clipText.startsWith("http://news.gdut.edu.cn/viewarticle.aspx?articleid=") -> {
                            id =
                                clipText.removePrefix("http://news.gdut.edu.cn/viewarticle.aspx?articleid=")
                        }
                        clipText.startsWith("https://news.gdut.edu.cn/viewarticle.aspx?articleid=") -> {
                            id =
                                clipText.removePrefix("https://news.gdut.edu.cn/viewarticle.aspx?articleid=")
                        }
                        clipText.startsWith("news.gdut.edu.cn/viewarticle.aspx?articleid=") -> {
                            id =
                                clipText.removePrefix("news.gdut.edu.cn/viewarticle.aspx?articleid=")
                        }
                    }
                    val intent = Intent(this@MainActivity, NewsDetailActivity::class.java)
                    intent.putExtra("id", id)
                    startActivity(intent)
                    GDUTNewsApplication.notToOpen = true
                }
                setNegativeButton("取消") { _, _ ->
                    GDUTNewsApplication.notToOpen = true
                }
                show()
            }
        }
    }

}
