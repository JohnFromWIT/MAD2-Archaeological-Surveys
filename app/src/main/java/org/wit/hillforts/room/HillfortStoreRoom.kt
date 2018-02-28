package org.wit.hillforts.room

import android.arch.persistence.room.Room
import android.content.Context
import org.jetbrains.anko.coroutines.experimental.bg
import org.wit.hillforts.models.HillfortStore
import org.wit.hillforts.models.HillfortModel

class HillfortStoreRoom(val context: Context) : HillfortStore {

    var dao: HillfortDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "site_room.db")
                .fallbackToDestructiveMigration()
                .build()
        dao = database.hillfortDao()
    }


    suspend override fun findAll(): List<HillfortModel> {
        val deferredHillforts = bg {
            dao.findAll()
        }
        val hillforts = deferredHillforts.await()
        return hillforts
    }

//    suspend override fun findTown(town: String): List<HillfortModel> {
//        val deferredHillforts = bg {
//            dao.findTown(town)
//        }
//        val hillforts = deferredHillforts.await()
//        return hillforts
//    }


    override fun create(hillfort: HillfortModel) {
        bg {
            dao.create(hillfort)
        }
    }

    override fun update(hillfort: HillfortModel) {
        bg{
            dao.update(hillfort)
        }
    }

    override fun delete(hillfort: HillfortModel) {
        bg {
            dao.deleteHillfort(hillfort)
        }
    }
}