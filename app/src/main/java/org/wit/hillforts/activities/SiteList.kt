package org.wit.hillforts.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.*
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_site_list.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.wit.hillforts.R
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel


class SiteList : AppCompatActivity(), HillfortListener {

    lateinit var app: MainApp

    //Activity start
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site_list)
        app = application as MainApp

        toolbarMain.title = title
        setSupportActionBar(toolbarMain)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        loadHillforts()
//        recyclerView.adapter = HillfortAdapter(app.hillforts.findAll(), this)

    }
    //Menu options created
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Recieve result from another activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadHillforts()
//        recyclerView.adapter.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }

    //Menu Option Selected
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult<Hillfort>(200)
            R.id.item_search -> {
                search()}
        }
        return super.onOptionsItemSelected(item)
    }

    //Search - Not fully implemented
    fun search(){
        val alert = AlertDialog.Builder(this)
        var searchBox: EditText?=null

        with (alert) {
            setTitle("Search")
            setMessage("Search Townland")
            // Add any  input field here
            searchBox=EditText(context)
            searchBox!!.hint="Townland.."
            searchBox!!.inputType = InputType.TYPE_CLASS_TEXT

            setPositiveButton("Go") {
                dialog, whichButton ->
                //showMessage("display the game score or anything!")
                dialog.dismiss()
                val searchText = searchBox!!.text.toString()
                loadHillfortsByTown(searchText)
            }

            setNegativeButton("Cancel") {
                dialog, whichButton ->
                //showMessage("Close the game or anything!")
                dialog.dismiss()
            }
        }
        val dialog = alert.create()
        dialog.setView(searchBox)
        dialog.show()
    }

    //Site Selected from list
    override fun onSiteClick(hillfort: HillfortModel) {
        startActivityForResult(intentFor<Hillfort>().putExtra("site_edit", hillfort), 200)
    }

    //Load list of sites
    private fun loadHillforts() {
        async(UI) {
            showHillforts(app.hillforts.findAll())
        }
    }

    //Load Search Results - Not Currently implemented
    private fun loadHillfortsByTown(town: String) {
        toast("Searched for "+ town)
//        async(UI) {
//            showHillforts(app.hillforts.sortByTownland())
//        }
    }

    //Update List
    fun showHillforts (hillforts: List<HillfortModel>) {
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter.notifyDataSetChanged()
    }

}
