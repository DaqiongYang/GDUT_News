package com.example.gdutnews

import android.app.Application
import android.content.Context

class GDUTNewsApplication: Application() {

    companion object{
        var session = ""
        var notToOpen = false
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}