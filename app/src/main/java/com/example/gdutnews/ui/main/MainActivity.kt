package com.example.gdutnews.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.gdutnews.GDUTNewsApplication
import com.example.gdutnews.R
import com.example.gdutnews.ui.detail.NewsDetailActivity
import com.example.gdutnews.ui.list.NewsListFrag
import com.example.gdutnews.ui.misc.GotoActivity
import com.example.gdutnews.ui.search.SearchActivity
import com.example.gdutnews.util.Clipboard
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val frag = newsList as NewsListFrag

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        viewModel.selectedItem = 4

        navView.setCheckedItem(R.id.xnztItem)
        navView.setNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.xnztItem -> viewModel.selectedItem = 4
                R.id.gsggItem -> viewModel.selectedItem = 5
                R.id.xnjxItem -> viewModel.selectedItem = 6
                R.id.zbggItem -> viewModel.selectedItem = 8
            }
            drawerLayout.closeDrawers()
            progressBar.visibility = View.VISIBLE
            viewModel.getItems(viewModel.selectedItem.toString())
            true
        }

        progressBar.visibility = View.VISIBLE

        viewModel.itemLiveData.observe(this) { result ->
            val content = result.getOrNull()
            if (content != null) {
                frag.init(viewModel.selectedItem, content, null)
            }
            progressBar.visibility = View.GONE
            when (viewModel.selectedItem) {
                4 -> supportActionBar?.title = "校内通知"
                5 -> supportActionBar?.title = "公示公告"
                6 -> supportActionBar?.title = "校内简讯"
                8 -> supportActionBar?.title = "招标公告"
            }
        }

        viewModel.getItems(viewModel.selectedItem.toString())
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
            R.id.searchItem -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
            R.id.gotoItem -> {
                val intent = Intent(this, GotoActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

}
