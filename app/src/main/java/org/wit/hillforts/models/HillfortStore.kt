package org.wit.hillforts.models


interface HillfortStore {
    suspend fun findAll(): List<HillfortModel>
    suspend fun findById(id:Long) : HillfortModel?

    fun create(hillfort: HillfortModel)
    fun update(hillfort: HillfortModel)
    fun delete(hillfort: HillfortModel)
    fun clear()

    //    suspend fun findTown(town: String): List<HillfortModel>
    //    suspend fun sortByTownland(town: String): List<HillfortModel>
}