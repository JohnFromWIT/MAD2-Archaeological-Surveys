package org.wit.hillforts.models
//Package should be models not model

import org.wit.hillforts.models.HillfortModel

interface HillfortStore {
    suspend fun findAll(): List<HillfortModel>
    fun create(hillfort: HillfortModel)
    fun update(hillfort: HillfortModel)
}