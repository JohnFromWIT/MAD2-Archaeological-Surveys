package org.wit.hillforts.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.hillforts.R
import org.wit.hillforts.helpers.*
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.models.Location
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class Hillfort : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()
    lateinit var app: MainApp
    lateinit var map: GoogleMap

    var edit = false
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    val defaultLocation = Location(52.245696, -7.139102, 15f)
    var location = Location(52.245696, -7.139102, 15f)

    private lateinit var locationService: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        mapView.onCreate(savedInstanceState);
        app = application as MainApp
        locationService = LocationServices.getFusedLocationProviderClient(this)

        toolbarSite.title = title
        setSupportActionBar(toolbarSite)

        btnHere.isEnabled = false

        //Map Box
        mapView.getMapAsync {
            map = it
            configureMap()
        }

        //If site exists import site details
        if (intent.hasExtra("site_edit")) {
            //If site already exists populate with existing
            edit = true
            hillfort = intent.extras.getParcelable<HillfortModel>("site_edit")
            siteTownland.setText(hillfort.townland)
            siteDateVisited.setText(hillfort.dateVisited)
            siteCounty.setText(hillfort.county)
            site_latitude.text = hillfort.lat.toString()
            site_longitude.text = hillfort.lng.toString()

            chooseImage.setImageBitmap(readImageFromPath(this, hillfort.picture))
//            if (hillfort.picture != null) {
//                btnAddImage.setText(R.string.button_changeImage)
//            }
        }else{
            hillfort.lat = defaultLocation.lat
            hillfort.lng = defaultLocation.lng
            hillfort.zoom = defaultLocation.zoom
        }


        btnHere.setOnClickListener {
            setCurrentLocation()
        }

        //Open image picker
        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }

        btnAddImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }


        //Open map
//        siteLocation.setOnClickListener {
//            if (hillfort.zoom != 0f) {
//                location.lat =  hillfort.lat
//                location.lng = hillfort.lng
//                location.zoom = hillfort.zoom
//            }
//            startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
//        }


//        site_longitude.setOnClickListener {
//            if (hillfort.zoom != 0f) {
//                location.lat =  hillfort.lat
//                location.lng = hillfort.lng
//                location.zoom = hillfort.zoom
//            }
//            startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
//        }
//
//        site_latitude.setOnClickListener {
//            if (hillfort.zoom != 0f) {
//                location.lat =  hillfort.lat
//                location.lng = hillfort.lng
//                location.zoom = hillfort.zoom
//            }
//            startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
//        }


//         //Delete site and close activity
//        btnDelete.setOnClickListener(){
//            deleteSite();
//        }


//        btnToday.setOnClickListener(){
//            val c = Calendar.getInstance()
//            val year = c.get(Calendar.YEAR)
//            val month = c.get(Calendar.MONTH)
//            val day = c.get(Calendar.DAY_OF_MONTH)
//            val date =  "" + day + "/" + month + "/" + year
//                // Display Selected date in textbox
//
//            siteDateVisited.setText(date)
//        }

        siteDateVisited.setOnClickListener(){
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, dateYear, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                val date =  "" + dayOfMonth + "/" + monthOfYear + "/" + dateYear
                siteDateVisited.setText(date)
            }, year, month, day)
            dpd.show()
        }
//        //Close hillfort activity
//        btnCancel.setOnClickListener(){
//            finish()
//        }
//
//        fun cancel(){
//            finish()
//        }


    }

    fun configureMap() {
        map.uiSettings.setZoomControlsEnabled(true)
        val loc = LatLng(hillfort.lat, hillfort.lng)
        val options = MarkerOptions().title(hillfort.townland).position(loc)
        map.addMarker(options)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, hillfort.zoom))
    }





    override fun onStart() {
        super.onStart()
        if (checkLocationPermissions(this)) {
            btnHere.isEnabled = true
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            btnHere.isEnabled = true
        }
    }

    @SuppressLint("MissingPermission")
    fun setCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            defaultLocation.lat = it.latitude
            defaultLocation.lng = it.longitude
            hillfort.lat = it.latitude
            hillfort.lng = it.longitude
            configureMap()
        }
    }

    fun deleteSite()
    {
        alert("Are you sure you want to DELETE this Hillfort?","Delete") {
            positiveButton("OK") {
                toast("Hillfort Deleted")
                app.hillforts.delete(hillfort)
                finish()
            }
            negativeButton("Keep"){
                toast("Delete Canceled")
            }
        }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            //If returning from image picker, update image details.
            IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.picture = data.getData().toString()
                    chooseImage.setImageBitmap(readImage(this, resultCode, data))
//                    btnAddImage.setText(R.string.button_changeImage)
                }
            }
            //If returning from map update location details
            LOCATION_REQUEST -> {
                if (data != null) {
                    val location = data.extras.getParcelable<Location>("location")
                    map.clear()
                    hillfort.lat = location.lat
                    hillfort.lng = location.lng
                    hillfort.zoom = location.zoom
//                    site_latitude.text = hillfort.lat.toString()
//                    site_longitude.text = hillfort.lng.toString()
                    configureMap()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_site, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun save(){
        hillfort.townland = siteTownland.text.toString()
        hillfort.dateVisited = siteDateVisited.text.toString()
        hillfort.county = siteCounty.text.toString()
        hillfort.lat =  location.lat
        hillfort.lng = location.lng
        hillfort.zoom = location.zoom
        if (edit) {
            app.hillforts.update(hillfort.copy())
            setResult(200)
            toast(R.string.toast_saved)
            finish()
        } else {
            if (hillfort.townland.isNotEmpty()) {
                app.hillforts.create(hillfort.copy())
                setResult(201)
                toast(R.string.toast_added)
                finish()
            } else {
                toast(R.string.toast_enter_data)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

            R.id.item_save -> {
                save()
            }
            R.id.item_delete -> {
                deleteSite()
            }
            R.id.item_cancel -> {
                alert("Are you sure?","Cancel") {
                    positiveButton("OK") {
                        toast("Changes Canceled")
                        finish()
                    }
                    negativeButton("Stay"){
                        toast("Changes Not Canceled")
                    }
                }.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}