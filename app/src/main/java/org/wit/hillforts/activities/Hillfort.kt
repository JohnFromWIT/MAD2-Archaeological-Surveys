package org.wit.hillforts.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import org.wit.hillforts.R
import org.wit.hillforts.helpers.readImage
import org.wit.hillforts.helpers.readImageFromPath
import org.wit.hillforts.helpers.showImagePicker
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel

class Hillfort : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()
    lateinit var app: MainApp
    var edit = false
    val IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        app = application as MainApp

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }

        if (intent.hasExtra("site_edit")) {
            //If site already exists populate with existing
            edit = true
            btnAdd.setText(R.string.button_saveSite)
            hillfort = intent.extras.getParcelable<HillfortModel>("site_edit")
            siteTownland.setText(hillfort.townland)
            siteDateVisited.setText(hillfort.dateVisited)
            
            hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.picture))
            if (hillfort.picture != null) {
                chooseImage.setText(R.string.change_siteImage)
            }
        }

        btnAdd.setOnClickListener() {

            hillfort.townland = siteTownland.text.toString()
            hillfort.dateVisited = siteDateVisited.text.toString()

            if (edit) {
                app.hillfortStore.update(hillfort.copy())
                setResult(200)
                finish()
            } else {
                if (hillfort.townland.isNotEmpty()) {
                    app.hillfortStore.create(hillfort.copy())
                    setResult(201)
                    finish()
                } else {
                    toast(R.string.toast_enter_data)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.picture = data.getData().toString()
                    hillfortImage.setImageBitmap(readImage(this, resultCode, data))
                }
            }
        }
    }
}