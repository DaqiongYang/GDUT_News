package com.example.gdutnews

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.news_list_frag.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException

class NewsListFrag : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.news_list_frag, container, false)
    }

    val update = 1

    val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        @SuppressLint("HandlerLeak")
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                update -> {
                    recyclerView.adapter?.notifyItemInserted(msg.arg2 - 1)
                    recyclerView.scrollToPosition(msg.arg1)
                    if (progressBar.visibility == View.VISIBLE) {
                        progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    fun init(id: Int = 0, rawData: String, info: Map<String, Any>? = null) {
        // id为0表示为搜索
        val newsList = ArrayList<NewsInfo>()
        var currentPage = 1
        var allPage = 0
        val doc = Jsoup.parse(rawData)
        val elements1 = doc.select("div[id=ContentPlaceHolder1_ListView1_ItemPlaceHolderContainer]")
        if (elements1.isEmpty()) {
            Toast.makeText(activity, "没有找到结果！", Toast.LENGTH_SHORT).show()
        } else {
            val titles = elements1.select("p")
            for (title in titles) {
                val newsInfo = title.select("a")
                val unit = title.select("span")
                newsList.add(
                    NewsInfo(
                        newsInfo[0].attr("href").removePrefix("./viewarticle.aspx?articleid="),
                        newsInfo[0].attr("title"),
                        unit[0].attr("title"),
                        unit[1].text().removeSuffix("&nbsp;").removeSuffix("]")
                    )
                )
            }
            allPage = doc.select("span[id=ContentPlaceHolder1_Label1]").text().toInt()
            val layoutManager = LinearLayoutManager(activity)
            recyclerView.layoutManager = layoutManager
            val adapter = NewsAdapter(newsList)
            recyclerView.adapter = adapter
        }


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            // 滑动加载
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val lm = recyclerView.layoutManager as LinearLayoutManager?
                val totalItemCount = recyclerView.adapter!!.itemCount
                val lastVisibleItemPosition = lm!!.findLastVisibleItemPosition()
                val visibleItemCount = recyclerView.childCount
                if ((newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition == totalItemCount - 1 && visibleItemCount > 0 && id != 0) or
                    (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition == totalItemCount - 1 && visibleItemCount > 0 && currentPage < allPage)
                ) {
                    // 执行刷新操作
                    val newPosition = newsList.size
                    if (progressBar.visibility == View.GONE) {
                        progressBar.visibility = View.VISIBLE
                    }
                    // 获取隐藏域
                    val selects: Elements = doc.select("input[type=hidden]")
                    val element1: Element = selects[0]
                    val __VIEWSTATE: String = element1.attr("value")
                    val element2: Element = selects[1]
                    val __EVENTVALIDATION: String = element2.attr("value")
                    if (id == 0 && info != null) {
                        // 制作表单，搜索情况
                        val formBody = FormBody.Builder()
                            .add("__EVENTTARGET", "ctl00\$ContentPlaceHolder1\$LinkButton2")
                            .add("__EVENTARGUMENT", "")
                            .add("__VIEWSTATE", __VIEWSTATE)
                            .add("__EVENTVALIDATION", __EVENTVALIDATION)
                            .add("ctl00\$username", "")
                            .add("ctl00\$PassWord", "")
                            .add("ctl00\$CheckBox1", "on")
                            .add("ctl00\$ContentPlaceHolder1\$keyword", "${info["keyword"]}")
                            .add("ctl00\$ContentPlaceHolder1\$searchtype", "${info["searchType"]}")
                            .add(
                                "ctl00\$ContentPlaceHolder1\$searchDepartments",
                                "${info["department"]}"
                            )
                            .add(
                                "ctl00\$ContentPlaceHolder1\$searchCategories",
                                "${info["category"]}"
                            )
                            .add("ctl00\$ContentPlaceHolder1\$startDate", "${info["start"]}")
                            .add("ctl00\$ContentPlaceHolder1\$endDate", "${info["end"]}")
                            .add("ctl00\$ContentPlaceHolder1\$TextBox1", "$currentPage")
                            .build()
                        httpUtil.HttpUtil.httpGotoNextPage(
                            "http://news.gdut.edu.cn/SearchArticles.aspx?" +
                                    "keyword=${info["keyword"]}" +
                                    "&category=${info["category"]}" +
                                    "&department=${info["department"]}" +
                                    "&start=${info["start"]}" +
                                    "&end=${info["end"]}" +
                                    "&searchType=${info["searchType"]}",
                            formBody, Session.SessionID, object : Callback {
                                override fun onResponse(call: Call, response: Response) {
                                    val newData = response.body?.string()
                                    val newDoc = Jsoup.parse(newData)
                                    val elements2 =
                                        newDoc.select("div[id=ContentPlaceHolder1_ListView1_ItemPlaceHolderContainer]")
                                    val newTitlies = elements2.select("p")
                                    for (title in newTitlies) {
                                        val newsInfo = title.select("a")
                                        val unit = title.select("span")
                                        newsList.add(
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
                                    currentPage++
                                    val msg = Message()
                                    msg.what = update
                                    msg.arg1 = newPosition
                                    msg.arg2 = newsList.size
                                    handler.sendMessage(msg)
                                }

                                override fun onFailure(call: Call, e: IOException) {
                                    e.printStackTrace()
                                }
                            })
                    } else {
                        // 制作表单， 非搜索情况
                        val formBody = FormBody.Builder()
                            .add("__VIEWSTATE", __VIEWSTATE)
                            .add("__EVENTVALIDATION", __EVENTVALIDATION)
                            .add("ctl00\$username", "")
                            .add("ctl00\$PassWord", "")
                            .add("ctl00\$CheckBox1", "on")
                            .add("ctl00\$ContentPlaceHolder1\$keyword", "")
                            .add("ctl00\$ContentPlaceHolder1\$searchDepartments", "2147483647")
                            .add("ctl00\$ContentPlaceHolder1\$searchCategories", "4")
                            .add("ctl00\$ContentPlaceHolder1\$startDate", "")
                            .add("ctl00\$ContentPlaceHolder1\$endDate", "")
                            .add("ctl00\$ContentPlaceHolder1\$TextBox1", "$currentPage")
                            .add("ctl00\$ContentPlaceHolder1\$Button_next1", "下一页")
                            .build()
                        httpUtil.HttpUtil.httpGotoNextPage(
                            "http://news.gdut.edu.cn/ArticleList.aspx?category=$id",
                            formBody, Session.SessionID, object : Callback {
                                override fun onResponse(call: Call, response: Response) {
                                    val newData = response.body?.string()
                                    val newDoc = Jsoup.parse(newData)
                                    val elements2 =
                                        newDoc.select("div[id=ContentPlaceHolder1_ListView1_ItemPlaceHolderContainer]")
                                    val newTitlies = elements2.select("p")
                                    for (title in newTitlies) {
                                        val newsInfo = title.select("a")
                                        val unit = title.select("span")
                                        newsList.add(
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
                                    currentPage++
                                    val msg = Message()
                                    msg.what = update
                                    msg.arg1 = newPosition
                                    msg.arg2 = newsList.size
                                    handler.sendMessage(msg)
                                }

                                override fun onFailure(call: Call, e: IOException) {
                                    e.printStackTrace()
                                }
                            })
                    }
                }
            }
        })
    }
}