package org.wit.hillforts.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import org.wit.hillforts.R
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel

class Hillfort : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        app = application as MainApp

        if (intent.hasExtra("hillfort_edit")) {
            //If site already exists populate with existing
            hillfort = intent.extras.getParcelable<HillfortModel>("hillfort_edit")
            siteTownland.setText(hillfort.townland)
            siteDateVisited.setText(hillfort.dateVisited)
        }
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        btnAdd.setOnClickListener() {

            hillfort.townland = siteTownland.text.toString()
            hillfort.dateVisited = siteDateVisited.text.toString()

            if (hillfort.townland.isNotEmpty()) {

                if (intent.hasExtra("hillfort_edit")) {
                    app.hillfortStore.update(hillfort)
                    setResult(AppCompatActivity.RESULT_OK)
                    finish()

                } else {
                    app.hillfortStore.create(hillfort.copy())
                    setResult(AppCompatActivity.RESULT_OK)
                    finish()
                }
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}