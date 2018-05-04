package org.wit.hillforts.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.wit.hillforts.activities.SplashScreen
import org.wit.hillforts.firebase.HillfortFireStore
import org.wit.hillforts.models.HillfortMemStore
import org.wit.hillforts.models.HillfortStore
import org.wit.hillforts.room.HillfortStoreRoom

class MainApp : Application(), AnkoLogger {

    lateinit var hillforts: HillfortStore

    //Start App and connect storeroom
    override fun onCreate() {
        super.onCreate()
//        hillforts = HillfortMemStore()
//         hillforts = HillfortStoreRoom (applicationContext)
        hillforts = HillfortFireStore(this)
        info("App Runing")
        startActivity<SplashScreen>()
    }
}