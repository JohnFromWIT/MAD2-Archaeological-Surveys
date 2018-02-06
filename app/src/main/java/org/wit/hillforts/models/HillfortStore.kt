package org.wit.hillforts.model

import org.wit.hillforts.models.HillfortModel

interface HillfortStore {
    fun findAll(): List<HillfortModel>
    fun create(hillfort: HillfortModel)
    fun update(hillfort: HillfortModel)
}