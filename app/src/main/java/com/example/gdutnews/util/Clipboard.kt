package com.example.gdutnews.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import com.example.gdutnews.GDUTNewsApplication

object Clipboard {

    private val cm =
        GDUTNewsApplication.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    fun copyToClipboard(text: String) {
        val cbData = ClipData.newPlainText("url", text)
        cm.setPrimaryClip(cbData)
        Toast.makeText(GDUTNewsApplication.context, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
    }

    fun getClipboard(): String {
        val result = ReadClipboardTask().execute().get()
        return result
    }

    class ReadClipboardTask : AsyncTask<Unit, Unit, String>() {
        // 由于受系统限制，必须开启线程等待约1s后才能读取，再通过异步消息处理机制回到主线程进行读取剪贴板的逻辑
        override fun doInBackground(vararg params: Unit?): String {
            Thread.sleep(1500)
            val clipText = cm.primaryClip?.getItemAt(0)?.text.toString().toLowerCase()
            return clipText
        }
    }
}