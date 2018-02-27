package org.wit.hillforts.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.activity_site_list.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.wit.hillforts.R
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel


class SiteList : AppCompatActivity(), HillfortListener {

    lateinit var app: MainApp

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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadHillforts()
//        recyclerView.adapter.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult<Hillfort>(200)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSiteClick(hillfort: HillfortModel) {
        startActivityForResult(intentFor<Hillfort>().putExtra("site_edit", hillfort), 200)
    }

    private fun loadHillforts() {
        async(UI) {
            showHillforts(app.hillforts.findAll())
        }
    }

    fun showHillforts (placemarks: List<HillfortModel>) {
        recyclerView.adapter = HillfortAdapter(placemarks, this)
        recyclerView.adapter.notifyDataSetChanged()
    }

}
