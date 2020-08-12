package com.example.gdutnews.ui.misc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.gdutnews.ui.detail.NewsDetailActivity
import com.example.gdutnews.R
import kotlinx.android.synthetic.main.activity_goto.*

class GotoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goto)
        gotoBtn.setOnClickListener {
            val address = addressText.text.toString().toLowerCase()
            var id : String? = null
            when {
                address.startsWith("http://news.gdut.edu.cn/viewarticle.aspx?articleid=") -> {
                    id = address.replace("http://news.gdut.edu.cn/viewarticle.aspx?articleid=", "")
                }
                address.startsWith("https://news.gdut.edu.cn/viewarticle.aspx?articleid=") -> {
                    id = address.replace("https://news.gdut.edu.cn/viewarticle.aspx?articleid=", "")
                }
                address.startsWith("news.gdut.edu.cn/viewarticle.aspx?articleid=") -> {
                    id = address.replace("news.gdut.edu.cn/viewarticle.aspx?articleid=", "")
                }
            }
            if (id != null){
                val intent = Intent(this, NewsDetailActivity::class.java)
                intent.putExtra("id", id)
                this.startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "链接无效，请重试！", Toast.LENGTH_SHORT).show()
            }
        }
    }
}