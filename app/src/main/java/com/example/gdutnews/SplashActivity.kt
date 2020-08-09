package com.example.gdutnews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import httpUtil.CallBackListener
import httpUtil.HttpUtil
import java.lang.Exception

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)       //设置沉浸式状态栏，在MIUI系统中，状态栏背景透明。原生系统中，状态栏背景半透明。
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)   //设置沉浸式虚拟键，在MIUI系统中，虚拟键背景透明。原生系统中，虚拟键背景半透明。

        HttpUtil.initSessionId(object : CallBackListener {
            override fun onFinish(session: String) {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                Session.SessionID = session
                startActivity(intent)
                finish()
            }

            override fun onError(e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@SplashActivity, "无法连接至通知网服务器，请检查网络后再试！", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                    finish()
                }
            }
        })
    }
}