package com.example.gdutnews

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import httpUtil.HttpUtil
import kotlinx.android.synthetic.main.activity_category.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


class CategoryActivity : AppCompatActivity() {

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        when (intent.getIntExtra("categoryID", 0)) {
            4 -> supportActionBar?.title = "校内通知"
            5 -> supportActionBar?.title = "公示公告"
            6 -> supportActionBar?.title = "校内简讯"
            8 -> supportActionBar?.title = "招标公告"
            else -> supportActionBar?.title = "错误"
        }
        val category = intent.getIntExtra("categoryID", 0)
        HttpUtil.httpRequest("http://news.gdut.edu.cn/ArticleList.aspx?category=$category",
            Session.SessionID, object :
                Callback {
                override fun onResponse(call: Call, response: Response) {
                    val content = response.body?.string()
                    runOnUiThread {
                        val frag = newsListFrag as NewsListFrag
                        content?.let {
                            frag.init(category, content, null)
                        }
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->   {
                finish()
            }
        }
        return true
    }
}