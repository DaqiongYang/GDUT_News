package com.example.gdutnews.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.inputmethodservice.InputMethodService
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.gdutnews.R
import com.example.gdutnews.ui.list.NewsListFrag
import kotlinx.android.synthetic.main.activity_search.*


class SearchActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(toolbar)
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
                viewModel.typeID = when (typeList[position]) {
                    "文章标题" -> "title"
                    "文章内容" -> "content"
                    "全文搜索" -> "article"
                    else -> "title"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.typeID = "title"
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
                    "所有部门" -> viewModel.departmentID = 2147483647
                    "党委办公室" -> viewModel.departmentID = 1
                    "校长办公室" -> viewModel.departmentID = 83
                    "机关党委" -> viewModel.departmentID = 77
                    "督办专办工作办公室" -> viewModel.departmentID = 78
                    "保密委员会办公室" -> viewModel.departmentID = 82
                    "纪委办公室、监察处" -> viewModel.departmentID = 7
                    "党委组织部" -> viewModel.departmentID = 8
                    "党委统战部" -> viewModel.departmentID = 2
                    "党委宣传部" -> viewModel.departmentID = 9
                    "发展规划处、学科建设办公室、高等教育研究所" -> viewModel.departmentID = 69
                    "教务处" -> viewModel.departmentID = 12
                    "招生办公室" -> viewModel.departmentID = 13
                    "科技与人文研究院" -> viewModel.departmentID = 14
                    "党委学生工作部、党委研究生工作部、学生工作处" -> viewModel.departmentID = 15
                    "研究生院" -> viewModel.departmentID = 16
                    "学位办公室" -> viewModel.departmentID = 17
                    "党委教师工作部、人事处" -> viewModel.departmentID = 18
                    "计划生育办公室" -> viewModel.departmentID = 19
                    "财务处" -> viewModel.departmentID = 20
                    "国有资产管理办公室" -> viewModel.departmentID = 21
                    "审计处" -> viewModel.departmentID = 22
                    "党委保卫部、保卫处" -> viewModel.departmentID = 23
                    "国际合作与交流处、港澳台事务办公室" -> viewModel.departmentID = 24
                    "实验室与设备管理处" -> viewModel.departmentID = 25
                    "招投标中心" -> viewModel.departmentID = 70
                    "基建处" -> viewModel.departmentID = 26
                    "后勤管理处" -> viewModel.departmentID = 27
                    "离退休工作处" -> viewModel.departmentID = 28
                    "关心下一代工作委员会" -> viewModel.departmentID = 29
                    "工会" -> viewModel.departmentID = 10
                    "团委" -> viewModel.departmentID = 11
                    "对外联络与校友事务办公室" -> viewModel.departmentID = 4
                    "机电工程学院" -> viewModel.departmentID = 30
                    "自动化学院" -> viewModel.departmentID = 31
                    "轻工化工学院" -> viewModel.departmentID = 32
                    "信息工程学院" -> viewModel.departmentID = 33
                    "土木与交通工程学院" -> viewModel.departmentID = 34
                    "管理学院" -> viewModel.departmentID = 35
                    "计算机学院" -> viewModel.departmentID = 36
                    "材料与能源学院" -> viewModel.departmentID = 37
                    "环境科学与工程学院" -> viewModel.departmentID = 38
                    "外国语学院" -> viewModel.departmentID = 39
                    "应用数学学院" -> viewModel.departmentID = 40
                    "物理与光电工程学院" -> viewModel.departmentID = 41
                    "艺术与设计学院" -> viewModel.departmentID = 42
                    "政法学院" -> viewModel.departmentID = 43
                    "马克思主义学院" -> viewModel.departmentID = 75
                    "建筑与城市规划学院" -> viewModel.departmentID = 44
                    "经济与贸易学院" -> viewModel.departmentID = 45
                    "生物医药学院" -> viewModel.departmentID = 74
                    "国际教育学院" -> viewModel.departmentID = 46
                    "继续教育学院" -> viewModel.departmentID = 47
                    "创新创业学院" -> viewModel.departmentID = 72
                    "体育部" -> viewModel.departmentID = 49
                    "实验教学部" -> viewModel.departmentID = 50
                    "通识教育中心、文化素质教育中心" -> viewModel.departmentID = 51
                    "国际文化教育中心" -> viewModel.departmentID = 64
                    "环境生态工程研究院" -> viewModel.departmentID = 79
                    "图书馆" -> viewModel.departmentID = 52
                    "档案馆" -> viewModel.departmentID = 53
                    "网络信息与现代教育技术中心" -> viewModel.departmentID = 54
                    "课室管理中心" -> viewModel.departmentID = 71
                    "校园卡管理中心" -> viewModel.departmentID = 80
                    "期刊中心" -> viewModel.departmentID = 55
                    "分析测试中心" -> viewModel.departmentID = 73
                    "本科教学质量评估与建设中心" -> viewModel.departmentID = 76
                    "产业技术研究与开发院" -> viewModel.departmentID = 81
                    "广东工大资产经营有限公司" -> viewModel.departmentID = 61
                    "医院" -> viewModel.departmentID = 60
                    "共建工作办公室" -> viewModel.departmentID = 84
                    else -> viewModel.departmentID = 2147483647
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                viewModel.departmentID = 2147483647
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
                    "所有分类" -> viewModel.categoryID = 2147483647
                    "校内通知" -> viewModel.categoryID = 4
                    "公示公告" -> viewModel.categoryID = 5
                    "校内简讯" -> viewModel.categoryID = 6
                    "招标公告" -> viewModel.categoryID = 8
                    else -> viewModel.categoryID = 2147483647
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.categoryID = 2147483647
            }
        }

        search_button.setOnClickListener {
            if (searchLayout.visibility != View.GONE) {
                searchLayout.visibility = View.GONE
                showOptBtn.visibility = View.VISIBLE
            }
            val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(searchLayout.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            getResult()
        }

        showOptBtn.setOnClickListener {
            if (searchLayout.visibility != View.VISIBLE) {
                searchLayout.visibility = View.VISIBLE
                showOptInPanel.visibility = View.VISIBLE
                showOptBtn.visibility = View.GONE
            }
        }

        showOptInPanel.setOnClickListener {
            if (searchLayout.visibility != View.GONE) {
                searchLayout.visibility = View.GONE
                showOptBtn.visibility = View.VISIBLE
            }
        }

        viewModel.result.observe(this) { result ->
            val data = result.getOrNull()
            val frag = resultListFrag as NewsListFrag
            val info = mapOf(
                "keyword" to searchText.text.toString(),
                "category" to viewModel.categoryID,
                "department" to viewModel.departmentID,
                "start" to startDate.text.toString(),
                "end" to endDate.text.toString(),
                "searchType" to viewModel.typeID
            )
            if (data != null) {
                frag.init(0, data, info)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }

    private fun getResult() {

        viewModel.url = "http://news.gdut.edu.cn/SearchArticles.aspx?" +
                "keyword=${searchText.text}" +
                "&category=${viewModel.categoryID}" +
                "&department=${viewModel.departmentID}" +
                "&start=${startDate.text}" +
                "&end=${endDate.text}" +
                "&searchType=${viewModel.typeID}"

        viewModel.getResult()
    }
}