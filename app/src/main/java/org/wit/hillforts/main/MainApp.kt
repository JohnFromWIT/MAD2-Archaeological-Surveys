package org.wit.hillforts.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillforts.activities.Hillfort
import org.wit.hillforts.models.HillfortModel

class MainApp : Application(), AnkoLogger {

    val hillforts = ArrayList<Hillfort>()

    override fun onCreate() {
        super.onCreate()
        info("App Runing")


    }
}