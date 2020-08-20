package com.example.gdutnews.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gdutnews.R
import com.example.gdutnews.logic.model.NewsInfo
import com.example.gdutnews.util.HiddenInputHelper
import kotlinx.android.synthetic.main.news_list_frag.*
import kotlinx.android.synthetic.main.news_list_frag.view.*
import okhttp3.FormBody
import org.jsoup.Jsoup

class NewsListFrag : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(NewsListViewModel::class.java) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.news_list_frag, container, false)
        view.backToTop.visibility = View.VISIBLE
        view.backToTop.setOnClickListener {
            recyclerView.scrollToPosition(0)
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.result.observe(this) { result ->
            refresh(result)
        }

        viewModel.searchResult.observe(this) { result ->
            refresh(result)
        }

    }

    fun init(id: Int = 0, rawData: String, info: Map<String, Any>? = null) {
        // id为0表示为搜索

        viewModel.categoryID = id.toString()
        viewModel.hiddenInput = HiddenInputHelper.getHiddenInfo(rawData)
        if (info != null) {
            viewModel.info = info
        }

        viewModel.currentPage = 1
        viewModel.newsList.clear()

        // 不要反复创建 adapter，否则会有多余的数据
        if (recyclerView.layoutManager == null) {
            val layoutManager = LinearLayoutManager(activity)
            recyclerView.layoutManager = layoutManager
        }
        if (recyclerView.adapter == null) {
            val adapter = NewsAdapter(viewModel.newsList)
            recyclerView.adapter = adapter
        }

        val doc = Jsoup.parse(rawData)
        val elements1 = doc.select("div[id=ContentPlaceHolder1_ListView1_ItemPlaceHolderContainer]")
        if (elements1.isEmpty()) {
            Toast.makeText(activity, "没有找到结果！", Toast.LENGTH_SHORT).show()
        } else {
            val titles = elements1.select("p")
            for (title in titles) {
                val newsInfo = title.select("a")
                val unit = title.select("span")
                viewModel.newsList.add(
                    NewsInfo(
                        newsInfo[0].attr("href").removePrefix("./viewarticle.aspx?articleid="),
                        newsInfo[0].attr("title"),
                        unit[0].attr("title"),
                        unit[1].text().removeSuffix("&nbsp;").removeSuffix("]")
                    )
                )
            }
            viewModel.allPage = doc.select("span[id=ContentPlaceHolder1_Label1]").text().toInt()
        }
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.adapter?.notifyItemInserted(viewModel.newsList.size - 1)
        recyclerView.scrollToPosition(0)


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            // 滑动加载
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val lm = recyclerView.layoutManager as LinearLayoutManager?
                val totalItemCount = recyclerView.adapter!!.itemCount
                val lastVisibleItemPosition = lm!!.findLastVisibleItemPosition()
                val visibleItemCount = recyclerView.childCount
                if ((newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition == totalItemCount - 1 && visibleItemCount > 0 && id != 0) or
                    (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition == totalItemCount - 1 && visibleItemCount > 0 && viewModel.currentPage.toInt() < viewModel.allPage)
                ) {

                    // 执行刷新操作
                    viewModel.newPosition = viewModel.newsList.size
                    if (progressBar.visibility == View.GONE) {
                        progressBar.visibility = View.VISIBLE
                    }

                    if (id == 0 && viewModel.info != null) {
                        // 制作表单，搜索情况
                        val formBody = FormBody.Builder()
                            .add("__EVENTTARGET", "ctl00\$ContentPlaceHolder1\$LinkButton2")
                            .add("__EVENTARGUMENT", "")
                            .add("__VIEWSTATE", viewModel.hiddenInput!!.__VIEWSTATE)
                            .add("__EVENTVALIDATION", viewModel.hiddenInput!!.__EVENTVALIDATION)
                            .add("ctl00\$username", "")
                            .add("ctl00\$PassWord", "")
                            .add("ctl00\$CheckBox1", "on")
                            .add(
                                "ctl00\$ContentPlaceHolder1\$keyword",
                                "${viewModel.info!!["keyword"]}"
                            )
                            .add(
                                "ctl00\$ContentPlaceHolder1\$searchtype",
                                "${viewModel.info!!["searchType"]}"
                            )
                            .add(
                                "ctl00\$ContentPlaceHolder1\$searchDepartments",
                                "${viewModel.info!!["department"]}"
                            )
                            .add(
                                "ctl00\$ContentPlaceHolder1\$searchCategories",
                                "${viewModel.info!!["category"]}"
                            )
                            .add(
                                "ctl00\$ContentPlaceHolder1\$startDate",
                                "${viewModel.info!!["start"]}"
                            )
                            .add(
                                "ctl00\$ContentPlaceHolder1\$endDate",
                                "${viewModel.info!!["end"]}"
                            )
                            .add(
                                "ctl00\$ContentPlaceHolder1\$TextBox1",
                                "${viewModel.currentPage + 1}"
                            )
                            .build()
                        val address = "http://news.gdut.edu.cn/SearchArticles.aspx?" +
                                "keyword=${viewModel.info!!["keyword"]}" +
                                "&category=${viewModel.info!!["category"]}" +
                                "&department=${viewModel.info!!["department"]}" +
                                "&start=${viewModel.info!!["start"]}" +
                                "&end=${viewModel.info!!["end"]}" +
                                "&searchType=${viewModel.info!!["searchType"]}"

                        viewModel.address = address
                        viewModel.getSearchPage(formBody)

                    } else {
                        // 制作表单， 非搜索情况
                        val formBody = FormBody.Builder()
                            .add("__VIEWSTATE", viewModel.hiddenInput!!.__VIEWSTATE)
                            .add("__EVENTVALIDATION", viewModel.hiddenInput!!.__EVENTVALIDATION)
                            .add("ctl00\$username", "")
                            .add("ctl00\$PassWord", "")
                            .add("ctl00\$CheckBox1", "on")
                            .add("ctl00\$ContentPlaceHolder1\$keyword", "")
                            .add("ctl00\$ContentPlaceHolder1\$searchDepartments", "2147483647")
                            .add("ctl00\$ContentPlaceHolder1\$searchCategories", "4")
                            .add("ctl00\$ContentPlaceHolder1\$startDate", "")
                            .add("ctl00\$ContentPlaceHolder1\$endDate", "")
                            .add(
                                "ctl00\$ContentPlaceHolder1\$TextBox1",
                                "${viewModel.currentPage + 1}"
                            )
                            .add("ctl00\$ContentPlaceHolder1\$Button_jump1", "跳转")
                            .build()

                        viewModel.getPage(formBody)

                    }
                }
            }
        })
    }

    private fun refresh(result: Result<String?>) {
        val newData = result.getOrNull()
        val newDoc = Jsoup.parse(newData)
        val elements2 =
            newDoc.select("div[id=ContentPlaceHolder1_ListView1_ItemPlaceHolderContainer]")
        val newTitlies = elements2.select("p")
        for (title in newTitlies) {
            val newsInfo = title.select("a")
            val unit = title.select("span")
            viewModel.newsList.add(
                NewsInfo(
                    newsInfo[0].attr("href")
                        .removePrefix("./viewarticle.aspx?articleid="),
                    newsInfo[0].attr("title"),
                    unit[0].attr("title"),
                    unit[1].text().removeSuffix("&nbsp;")
                        .removeSuffix("]")
                )
            )
        }
        viewModel.currentPage = viewModel.currentPage + 1
        recyclerView.adapter?.notifyItemInserted(viewModel.newsList.size - 1)
        recyclerView.scrollToPosition(viewModel.newPosition)
        if (progressBar.visibility == View.VISIBLE) progressBar.visibility = View.GONE
    }
}