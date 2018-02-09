package org.wit.hillforts.activities

import android.support.v7.widget.RecyclerView
import android.view.View
import org.wit.hillforts.models.HillfortModel


/**
 * Created by chris on 06/02/2018.
 */
interface HillfortListener {
    fun onPlacemarkClick(hillfort: HillfortModel)
}


class HillfortAdapter constructor(private var placemarks: List<HillfortModel>){

                                   }

