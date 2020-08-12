package com.example.gdutnews.ui.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.gdutnews.R
import kotlinx.android.synthetic.main.activity_category.*


class CategoryActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(CategoryViewModel::class.java) }

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

        viewModel.itemLiveData.observe(this) { result ->
            val content = result.getOrNull()
            val frag = newsListFrag as NewsListFrag
            if (content != null) {
                frag.init(category, content, null)
            }
        }
            viewModel.getItems(category.toString())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}