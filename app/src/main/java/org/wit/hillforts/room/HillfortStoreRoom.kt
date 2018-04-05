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

    //coroutine - request all and await result
    suspend override fun findAll(): List<HillfortModel> {
        val deferredHillforts = bg {
            dao.findAll()
        }
        val hillforts = deferredHillforts.await()
        return hillforts
    }

    //coroutine search - Not Currently Implemented
//    suspend override fun findTown(town: String): List<HillfortModel> {
//        val deferredHillforts = bg {
//            dao.findTown(town)
//        }
//        val hillforts = deferredHillforts.await()
//        return hillforts
//    }

    //coroutine sort - Not Currently Implemented
    suspend override fun sortByTownland(town: String): List<HillfortModel>{
        val sortedHillforts = bg {
            dao.sortByTownland(town)
        }
        val hillforts = sortedHillforts.await()
        return hillforts
    }

    //coroutine - create
    override fun create(hillfort: HillfortModel) {
        bg {
            dao.create(hillfort)
        }
    }

    //coroutine - update
    override fun update(hillfort: HillfortModel) {
        bg{
            dao.update(hillfort)
        }
    }

    //coroutine - delete
    override fun delete(hillfort: HillfortModel) {
        bg {
            dao.deleteHillfort(hillfort)
        }
    }

    override suspend fun findById(id: Long): HillfortModel? {
        val deferredHillfort = bg {
            dao.findById(id)
        }
        val hillfort = deferredHillfort.await()
        return hillfort
    }

}