package com.example.gdutnews

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NewsAdapter(val newsList: List<NewsInfo>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>(){

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val titleText : TextView = view.findViewById(R.id.titleText)
        val departmentText : TextView = view.findViewById(R.id.departmentText)
        val timeText : TextView = view.findViewById(R.id.timeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val newsID = newsList[position].ID
            val intent = Intent(parent.context, NewsDetailActivity::class.java)
            intent.putExtra("id", newsID)
            parent.context.startActivity(intent)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = newsList[position]
        holder.titleText.text = news.title
        holder.departmentText.text = news.department
        holder.timeText.text = news.time
    }

    override fun getItemCount(): Int {
        return newsList.size
    }
}