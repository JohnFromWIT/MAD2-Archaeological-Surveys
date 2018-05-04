package org.wit.hillforts.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.hillforts.R
import org.wit.hillforts.firebase.HillfortFireStore
import org.wit.hillforts.main.MainApp

class LoginActivity : AppCompatActivity(), AnkoLogger {

    lateinit var auth: FirebaseAuth
    var fireStore: HillfortFireStore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressBar.visibility = View.GONE
        auth = FirebaseAuth.getInstance()

        var app = application as MainApp
        if (app.hillforts is HillfortFireStore) {
            fireStore = app.hillforts as HillfortFireStore
        }

        signUpBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val email = field_email.text.toString()
            val password = field_password.text.toString()
            if (email == "" || password == "") {
                toast("Please provide email + password")
            }
            else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        info("Login success")
                        startActivity(intentFor<SiteList>())
                    }
                    else {
                        toast("Sign Up Failed: ${task.exception?.message}")
                    }
                }
            }
        }

        signInBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val email = field_email.text.toString()
            val password = field_password.text.toString()
            if (email == "" || password == "") {
                toast("Please provide email + password")
            }
            else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        if (fireStore != null) {
                            fireStore!!.fetchHillforts {
                                startActivity(intentFor<SiteList>())
                            }
                        }
                        else {
                            startActivity(intentFor<SiteList>())
                        }
                    }
                    else {
                        toast("Sign In Failed")
                    }
                }
            }
        }
    }
}