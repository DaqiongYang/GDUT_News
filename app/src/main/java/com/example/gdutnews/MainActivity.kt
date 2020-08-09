package com.example.gdutnews

import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    val clipboard = 1

    private val handler = @SuppressLint("HandlerLeak")
    object : Handler(){
        override fun handleMessage(msg: Message) {
            when(msg.what){
                clipboard -> {
                    showOpenDialog()
                }
            }
        }
    }

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
        // 这样子写就可以获得网页的内容了
        if (Session.SessionID != "") {
            val intent = Intent(this@MainActivity, CategoryActivity::class.java)
            intent.putExtra("categoryID", category)
            startActivity(intent)
            Session.notToOpen = true
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        // 在获取焦点时读取剪贴板文字，但是由于受系统限制，必须开启线程等待约1s后才能读取，再通过异步消息处理机制回到主线程进行读取剪贴板的逻辑
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus and !Session.notToOpen) {
            thread {
                Thread.sleep(1000)
                val msg = Message()
                msg.what = clipboard
                handler.sendMessage(msg)
            }
        }
    }

    private fun showOpenDialog(){
        // 读取剪贴板的逻辑
        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipText = cm.primaryClip?.getItemAt(0)?.text.toString().toLowerCase()
        if(clipText.startsWith("http://news.gdut.edu.cn/viewarticle.aspx?articleid=") or
            clipText.startsWith("news.gdut.edu.cn/viewarticle.aspx?articleid=") or
            clipText.startsWith("https://news.gdut.edu.cn/viewarticle.aspx?articleid=")){
            AlertDialog.Builder(this@MainActivity).apply {
                Log.d("debug", clipText)
                setTitle("是否跳转？")
                setMessage("检测到剪贴板里有通知网链接，是否打开")
                setPositiveButton("确定"){dialog, which ->
                    var id :String? = null
                    when {
                        clipText.startsWith("http://news.gdut.edu.cn/viewarticle.aspx?articleid=") -> {
                            id = clipText.removePrefix("http://news.gdut.edu.cn/viewarticle.aspx?articleid=")
                        }
                        clipText.startsWith("https://news.gdut.edu.cn/viewarticle.aspx?articleid=") -> {
                            id = clipText.removePrefix("https://news.gdut.edu.cn/viewarticle.aspx?articleid=")
                        }
                        clipText.startsWith("news.gdut.edu.cn/viewarticle.aspx?articleid=") -> {
                            id = clipText.removePrefix("news.gdut.edu.cn/viewarticle.aspx?articleid=")
                        }
                    }
                    Log.d("debug" ,"id:$id")
                    val intent = Intent(this@MainActivity, NewsDetailActivity::class.java)
                    intent.putExtra("id", id)
                    startActivity(intent)
                    Session.notToOpen = true
                }
                setNegativeButton("取消"){ _, _ ->
                    Session.notToOpen = true
                }
                show()
            }
        }
    }

}
