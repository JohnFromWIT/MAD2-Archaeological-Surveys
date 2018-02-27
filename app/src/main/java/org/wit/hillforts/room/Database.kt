package org.wit.hillforts.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import org.wit.hillforts.models.HillfortModel

@Database(entities = arrayOf(HillfortModel::class), version = 1)
abstract class Database : RoomDatabase() {

    abstract fun hillfortDao(): HillfortDao
}