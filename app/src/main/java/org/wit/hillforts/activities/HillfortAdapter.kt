package org.wit.hillforts.activities

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.card_site.view.*
import org.wit.hillforts.R
import org.wit.hillforts.helpers.readImageFromPath
import org.wit.hillforts.models.HillfortModel


/**
 * Created by chris on 06/02/2018.
 */
interface HillfortListener {
    fun onSiteClick(hillfort: HillfortModel)
    fun onSiteLongClick(hillfort: HillfortModel)
    fun onSiteNewRating(hillfort: HillfortModel, number: Number)
}

class HillfortAdapter constructor(private var hillforts: List<HillfortModel>,
                                   private val listener: HillfortListener) : RecyclerView.Adapter<HillfortAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_site, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hillfort = hillforts[holder.adapterPosition]
        holder.bind(hillfort, listener)
    }

    override fun getItemCount(): Int = hillforts.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(hillfort: HillfortModel, listener: HillfortListener) {
            itemView.siteTownland.text = hillfort.townland
            itemView.siteDateVisited.text = hillfort.dateVisited
            itemView.imageIcon.setImageBitmap(readImageFromPath(itemView.context, hillfort.picture))
            itemView.setOnClickListener { listener.onSiteClick(hillfort) }
            itemView.setOnLongClickListener { listener.onSiteLongClick(hillfort); true }
            itemView.siteRating.setOnClickListener{listener.onSiteNewRating(hillfort, itemView.siteRating.numStars)}
        }
    }
}