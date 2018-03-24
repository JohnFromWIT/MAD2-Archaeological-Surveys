package org.wit.hillforts.models


interface HillfortStore {
    suspend fun findAll(): List<HillfortModel>
    fun create(hillfort: HillfortModel)
    fun update(hillfort: HillfortModel)
    fun delete(hillfort: HillfortModel)
//    suspend fun findTown(town: String): List<HillfortModel>
//    suspend fun sortByTownland(): List<HillfortModel>
    suspend fun findById(id:Long) : HillfortModel?
}