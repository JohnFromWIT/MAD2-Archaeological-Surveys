package org.wit.hillforts.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.activity_site_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.hillforts.R
import org.wit.hillforts.helpers.readImage
import org.wit.hillforts.helpers.readImageFromPath
import org.wit.hillforts.helpers.showImagePicker
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.models.Location

class Hillfort : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()
    lateinit var app: MainApp
    var edit = false
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    var location = Location(52.245696, -7.139102, 15f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        app = application as MainApp

        toolbarSite.title = title
        setSupportActionBar(toolbarSite)
        
        //Open image picker
        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }

        btnAddImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }

        //Open map
        siteLocation.setOnClickListener {
            if (hillfort.zoom != 0f) {
                location.lat =  hillfort.lat
                location.lng = hillfort.lng
                location.zoom = hillfort.zoom
            }
            startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
        }

        //If site exists import site details
        if (intent.hasExtra("site_edit")) {
            //If site already exists populate with existing
            edit = true
            btnAdd.setText(R.string.button_saveSite)
            hillfort = intent.extras.getParcelable<HillfortModel>("site_edit")
            siteTownland.setText(hillfort.townland)
            siteDateVisited.setText(hillfort.dateVisited)
            siteCounty.setText(hillfort.county)
            site_latitude.text = hillfort.lat.toString()
            site_longitude.text = hillfort.lng.toString()

            chooseImage.setImageBitmap(readImageFromPath(this, hillfort.picture))
            if (hillfort.picture != null) {
                btnAddImage.setText(R.string.button_changeImage)
            }
        }

        //Add hilllfort model to list of sites
        btnAdd.setOnClickListener() {

            hillfort.townland = siteTownland.text.toString()
            hillfort.dateVisited = siteDateVisited.text.toString()
            hillfort.county = siteCounty.text.toString()
            hillfort.lat =  location.lat
            hillfort.lng = location.lng
            hillfort.zoom = location.zoom
            if (edit) {
                app.hillforts.update(hillfort.copy())
                setResult(200)
                finish()
            } else {
                if (hillfort.townland.isNotEmpty()) {
                    app.hillforts.create(hillfort.copy())
                    setResult(201)
                    finish()
                } else {
                    toast(R.string.toast_enter_data)
                }
            }
        }

        //Delete site and close activity
        btnDelete.setOnClickListener(){
            app.hillforts.delete(hillfort)
            finish()
        }

        //Close hillfort activity
        btnCancel.setOnClickListener(){
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            //If returning from iage picker, update image details.
            IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.picture = data.getData().toString()
                    chooseImage.setImageBitmap(readImage(this, resultCode, data))
                    btnAddImage.setText(R.string.button_changeImage)
                }
            }
            //If returning from map update location details
            LOCATION_REQUEST -> {
                if (data != null) {
                    val location = data.extras.getParcelable<Location>("location")
                    hillfort.lat = location.lat
                    hillfort.lng = location.lng
                    hillfort.zoom = location.zoom
                    site_latitude.text = hillfort.lat.toString()
                    site_longitude.text = hillfort.lng.toString()
                }
            }
        }
    }
}