package com.example.gdutnews

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_search.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


class SearchActivity : AppCompatActivity() {

    // 默认ID值
    var typeID = "title"
    var departmentID = 2147483647
    var categoryID = 2147483647

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 初始化下拉栏
        val typeList = listOf("文章标题", "文章内容", "全文搜索")
        val typeAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeList)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = typeAdapter
        typeSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                typeID = when (typeList[position]) {
                    "文章标题" -> "title"
                    "文章内容" -> "content"
                    "全文搜索" -> "article"
                    else -> "title"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                typeID = "title"
            }
        }

        val departmentList = listOf(
            "所有部门",
            "党委办公室",
            "校长办公室",
            "机关党委",
            "督办专办工作办公室",
            "保密委员会办公室",
            "纪委办公室、监察处",
            "党委组织部",
            "党委统战部",
            "党委宣传部",
            "发展规划处、学科建设办公室、高等教育研究所",
            "教务处",
            "招生办公室",
            "科技与人文研究院",
            "党委学生工作部、党委研究生工作部、学生工作处",
            "研究生院",
            "学位办公室",
            "党委教师工作部、人事处",
            "计划生育办公室",
            "财务处",
            "国有资产管理办公室",
            "审计处",
            "党委保卫部、,保卫处",
            "国际合作与交流处、港澳台事务办公室",
            "实验室与设备管理处",
            "招投标中心",
            "基建处",
            "后勤管理处",
            "离退休工作处",
            "关心下一代工作委员会",
            "工会",
            "团委",
            "对外联络与校友事务办公室",
            "机电工程学院",
            "自动化学院",
            "轻工化工学院",
            "信息工程学院",
            "土木与交通工程学院",
            "管理学院",
            "计算机学院",
            "材料与能源学院",
            "环境科学与工程学院",
            "外国语学院",
            "应用数学学院",
            "物理与光电工程学院",
            "艺术与设计学院",
            "政法学院",
            "马克思主义学院",
            "建筑与城市规划学院",
            "经济与贸易学院",
            "生物医药学院",
            "国际教育学院",
            "继续教育学院",
            "创新创业学院",
            "体育部",
            "实验教学部",
            "通识教育中心、文化素质教育中心",
            "国际文化教育中心",
            "环境生态工程研究院",
            "图书馆",
            "档案馆",
            "网络信息与现代教育技术中心",
            "课室管理中心",
            "校园卡管理中心",
            "期刊中心",
            "分析测试中心",
            "本科教学质量评估与建设中心",
            "产业技术研究与开发院",
            "广东工大资产经营有限公司",
            "医院",
            "共建工作办公室"
        )
        val departmentAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, departmentList)
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        departmentSpinner.adapter = departmentAdapter
        // 下拉列表事件的写法
        departmentSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // 在这里填写逻辑
                when (departmentList[position]) {
                    "所有部门" -> departmentID = 2147483647
                    "党委办公室" -> departmentID = 1
                    "校长办公室" -> departmentID = 83
                    "机关党委" -> departmentID = 77
                    "督办专办工作办公室" -> departmentID = 78
                    "保密委员会办公室" -> departmentID = 82
                    "纪委办公室、监察处" -> departmentID = 7
                    "党委组织部" -> departmentID = 8
                    "党委统战部" -> departmentID = 2
                    "党委宣传部" -> departmentID = 9
                    "发展规划处、学科建设办公室、高等教育研究所" -> departmentID = 69
                    "教务处" -> departmentID = 12
                    "招生办公室" -> departmentID = 13
                    "科技与人文研究院" -> departmentID = 14
                    "党委学生工作部、党委研究生工作部、学生工作处" -> departmentID = 15
                    "研究生院" -> departmentID = 16
                    "学位办公室" -> departmentID = 17
                    "党委教师工作部、人事处" -> departmentID = 18
                    "计划生育办公室" -> departmentID = 19
                    "财务处" -> departmentID = 20
                    "国有资产管理办公室" -> departmentID = 21
                    "审计处" -> departmentID = 22
                    "党委保卫部、保卫处" -> departmentID = 23
                    "国际合作与交流处、港澳台事务办公室" -> departmentID = 24
                    "实验室与设备管理处" -> departmentID = 25
                    "招投标中心" -> departmentID = 70
                    "基建处" -> departmentID = 26
                    "后勤管理处" -> departmentID = 27
                    "离退休工作处" -> departmentID = 28
                    "关心下一代工作委员会" -> departmentID = 29
                    "工会" -> departmentID = 10
                    "团委" -> departmentID = 11
                    "对外联络与校友事务办公室" -> departmentID = 4
                    "机电工程学院" -> departmentID = 30
                    "自动化学院" -> departmentID = 31
                    "轻工化工学院" -> departmentID = 32
                    "信息工程学院" -> departmentID = 33
                    "土木与交通工程学院" -> departmentID = 34
                    "管理学院" -> departmentID = 35
                    "计算机学院" -> departmentID = 36
                    "材料与能源学院" -> departmentID = 37
                    "环境科学与工程学院" -> departmentID = 38
                    "外国语学院" -> departmentID = 39
                    "应用数学学院" -> departmentID = 40
                    "物理与光电工程学院" -> departmentID = 41
                    "艺术与设计学院" -> departmentID = 42
                    "政法学院" -> departmentID = 43
                    "马克思主义学院" -> departmentID = 75
                    "建筑与城市规划学院" -> departmentID = 44
                    "经济与贸易学院" -> departmentID = 45
                    "生物医药学院" -> departmentID = 74
                    "国际教育学院" -> departmentID = 46
                    "继续教育学院" -> departmentID = 47
                    "创新创业学院" -> departmentID = 72
                    "体育部" -> departmentID = 49
                    "实验教学部" -> departmentID = 50
                    "通识教育中心、文化素质教育中心" -> departmentID = 51
                    "国际文化教育中心" -> departmentID = 64
                    "环境生态工程研究院" -> departmentID = 79
                    "图书馆" -> departmentID = 52
                    "档案馆" -> departmentID = 53
                    "网络信息与现代教育技术中心" -> departmentID = 54
                    "课室管理中心" -> departmentID = 71
                    "校园卡管理中心" -> departmentID = 80
                    "期刊中心" -> departmentID = 55
                    "分析测试中心" -> departmentID = 73
                    "本科教学质量评估与建设中心" -> departmentID = 76
                    "产业技术研究与开发院" -> departmentID = 81
                    "广东工大资产经营有限公司" -> departmentID = 61
                    "医院" -> departmentID = 60
                    "共建工作办公室" -> departmentID = 84
                    else -> departmentID = 2147483647
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                departmentID = 2147483647
            }
        }

        val categoryList = listOf("所有分类", "校内通知", "公示公告", "校内简讯", "招标公告")
        val categoryAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter
        categorySpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (categoryList[position]) {
                    "所有分类" -> categoryID = 2147483647
                    "校内通知" -> categoryID = 4
                    "公示公告" -> categoryID = 5
                    "校内简讯" -> categoryID = 6
                    "招标公告" -> categoryID = 8
                    else -> categoryID = 2147483647
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                categoryID = 2147483647
            }
        }

        search_button.setOnClickListener {
            if (searchLayout.visibility != View.GONE){
                searchLayout.visibility = View.GONE
                showOptBtn.visibility = View.VISIBLE
            }
            getResult()
        }

        showOptBtn.setOnClickListener {
            if (searchLayout.visibility != View.VISIBLE){
                searchLayout.visibility = View.VISIBLE
                showOptBtn.visibility = View.GONE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->   {
                finish()
            }
        }
        return true
    }

    private fun getResult() {
        val info = mapOf(
            "keyword" to searchText.text.toString(),
            "category" to categoryID,
            "department" to departmentID,
            "start" to startDate.text.toString(),
            "end" to endDate.text.toString(),
            "searchType" to typeID
        )
        httpUtil.HttpUtil.httpRequest("http://news.gdut.edu.cn/SearchArticles.aspx?" +
                "keyword=${searchText.text}" +
                "&category=${categoryID}" +
                "&department=${departmentID}" +
                "&start=${startDate.text}" +
                "&end=${endDate.text}" +
                "&searchType=${typeID}", Session.SessionID, object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val date = response.body?.string()
                runOnUiThread {
                    val frag = resultListFrag as NewsListFrag
                    if (date != null) {
                        frag.init(0, date, info)
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }
}