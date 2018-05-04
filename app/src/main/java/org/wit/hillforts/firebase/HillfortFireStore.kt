package org.wit.hillforts.firebase

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import org.wit.hillforts.helpers.readImageFromPath
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.models.HillfortStore
import java.io.ByteArrayOutputStream
import java.io.File

class HillfortFireStore(val context: Context) : HillfortStore, AnkoLogger {

    val hillforts = ArrayList<HillfortModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st : StorageReference

    suspend override fun findAll(): List<HillfortModel> {
        return hillforts
    }

    suspend override fun findById(id: Long): HillfortModel? {
        val foundHillfort: HillfortModel? = hillforts.find { p -> p.id == id }
        return foundHillfort
    }

    override fun create(hillfort: HillfortModel) {
        val key = db.child("users").child(userId).child("hillforts").push().key
        hillfort.fbId = key
        hillforts.add(hillfort)
        db.child("users").child(userId).child("hillforts").child(key).setValue(hillfort)
        updateImage(hillfort)
    }

    override fun update(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.fbId == hillfort.fbId }
        if (foundHillfort != null) {
            foundHillfort.townland = hillfort.townland
            foundHillfort.county = hillfort.county
            foundHillfort.picture = hillfort.picture
            foundHillfort.lat = hillfort.lat
            foundHillfort.lng = hillfort.lng
            foundHillfort.zoom = hillfort.zoom
        }

        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(hillfort)
        if ((hillfort.picture.length) > 0 && (hillfort.picture[0] != 'h')) {
            updateImage(hillfort)
        }
    }

    override fun delete(hillfort: HillfortModel) {
        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).removeValue()
        hillforts.remove(hillfort)
    }

    override fun clear() {
        hillforts.clear()
    }

    fun updateImage(hillfort: HillfortModel) {
        if (hillfort.picture != "") {
            val fileName = File(hillfort.picture)
            val imageName = fileName.getName()

            val bitmap = readImageFromPath(context, hillfort.picture)
            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            val uploadTask = imageRef.putBytes(data)
            uploadTask.addOnFailureListener {
                println (it.message)
            }.addOnSuccessListener { taskSnapshot ->
                hillfort.picture = taskSnapshot.downloadUrl.toString()
                db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(hillfort)
            }
        }
    }

    fun fetchHillforts(hillfortsReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                dataSnapshot!!.children.mapNotNullTo(hillforts) { it.getValue<HillfortModel>(HillfortModel::class.java) }
                hillfortsReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        hillforts.clear()
        db.child("users").child(userId).child("hillforts").addListenerForSingleValueEvent(valueEventListener)
    }

}