package com.example.gdutnews.ui.misc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.gdutnews.R
import com.example.gdutnews.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(SplashViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

        viewModel.sessionIDLiveData.observe(this){ result ->
            val sessionID = result.getOrNull()
            if (sessionID != null){
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this@SplashActivity, "无法连接至通知网服务器，请检查网络后再试！", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.getSessionID()

    }
}