package com.example.testfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UpdateActivity : AppCompatActivity() {

    var txtFirstname: EditText? = null
    var txtLastname: EditText? = null
    var btnUpdate: Button? = null

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        txtFirstname = findViewById(R.id.txtFirstname2)
        txtLastname = findViewById(R.id.txtLastname2)
        btnUpdate = findViewById(R.id.btnUpdate2)

        btnUpdate!!.setOnClickListener {
            var firstName = txtFirstname!!.text.toString()
            var lastName = txtLastname!!.text.toString()
            var intent = intent
            if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
                val user = hashMapOf(
                    "firstName" to firstName,
                    "lastName" to lastName,
                )

                val query = db.collection("users")
                    .whereEqualTo("firstName",intent.getStringExtra("firstName"))
                    .whereEqualTo("lastName",intent.getStringExtra("lastName"))
                    .get()
                query.addOnSuccessListener { result ->
                    for (document in result) {
                        db.collection("users").document(document.id).set(user, SetOptions.merge())
                    }
                    Toast.makeText(applicationContext, "User Information Update", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please insert value first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}
