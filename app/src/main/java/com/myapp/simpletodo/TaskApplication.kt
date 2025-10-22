package com.myapp.simpletodo

import android.app.Application
import com.myapp.simpletodo.di.AppContainer
import com.myapp.simpletodo.di.DefaultAppContainer

class TaskApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}