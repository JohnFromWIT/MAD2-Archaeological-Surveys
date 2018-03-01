package org.wit.hillforts.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillforts.models.HillfortStore
import org.wit.hillforts.room.HillfortStoreRoom
class MainApp : Application(), AnkoLogger {

    lateinit var hillforts: HillfortStore

    //Start App and connect storeroom
    override fun onCreate() {
        super.onCreate()
        hillforts = HillfortStoreRoom(applicationContext)
        info("App Runing")


    }
}