package org.wit.hillforts.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class HillfortMemStore : HillfortStore, AnkoLogger {

    val hillforts = ArrayList<HillfortModel>()

    //FInd All
    suspend override fun findAll(): List<HillfortModel> {
        return hillforts
    }

    //Create
    override fun create(hillfort: HillfortModel) {
        hillfort.id = getId()
        hillforts.add(hillfort)
        logAll()
    }

    //Update
    override fun update(hillfort: HillfortModel) {
        val foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.townland = hillfort.townland
            foundHillfort.county = hillfort.county
            foundHillfort.dateVisited = hillfort.dateVisited
            foundHillfort.picture = hillfort.picture
            foundHillfort.lat = hillfort.lat
            foundHillfort.lng = hillfort.lng
            foundHillfort.zoom = hillfort.zoom
        }
    }

    //Delete
    override fun delete(hillfort: HillfortModel) {
        hillforts.remove(hillfort)
    }

    override fun clear() {
        hillforts.clear()
    }

    //Log All
    internal fun logAll() {
        hillforts.forEach { info("${it}") }
    }

    //Search Method - Not Currently Implemented
//    suspend override fun findTown(town: String): List<HillfortModel> {
//        return hillforts
//    }

    //Sort Method - Not Currently Implemented
//    suspend override fun sortByTownland(town: String): List<HillfortModel>{
//       return hillforts
//    }

    suspend override fun findById(id:Long) : HillfortModel? {
        val foundHillfort: HillfortModel? = hillforts.find { p -> p.id == id }
        return foundHillfort
    }
}