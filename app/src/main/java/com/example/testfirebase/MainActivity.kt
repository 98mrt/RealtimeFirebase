package com.example.testfirebase

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    var txtFirstname: EditText? = null
    var txtLastname: EditText? = null
    var btnInsert: Button? = null
    var btnUpdate: Button? = null
    var btnDelete: Button? = null
    var btnRead: Button? = null
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtFirstname = findViewById(R.id.txtFirstname)
        txtLastname = findViewById(R.id.txtLastname)
        btnInsert = findViewById(R.id.btnInsert)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
        btnRead = findViewById(R.id.btnRead)

        btnUpdate!!.setOnClickListener {
            var firstName = txtFirstname!!.text.toString()
            var lastName = txtLastname!!.text.toString()
            if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
                    val intent = Intent(this,UpdateActivity::class.java)
                    intent.putExtra("firstName",firstName)
                    intent.putExtra("lastName",lastName)
                    startActivity(intent)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please insert value first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnRead!!.setOnClickListener {
            val data = StringBuffer()
            db.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        data.append("User ID : " + document.id + "\n")
                        data.append("User First Name : " + document.get("firstName") + "\n")
                        data.append("User Last Name : " + document.get("lastName") + "\n\n")
                        data.append("==============================\n")
                    }
                    displayMessage("Users Information", data.toString())
                }
                .addOnFailureListener { e ->
                    Toast.makeText(applicationContext, "Error: " + e.toString(), Toast.LENGTH_SHORT).show()
                }
        }

        btnInsert!!.setOnClickListener {
            var firstName = txtFirstname!!.text.toString()
            var lastName = txtLastname!!.text.toString()
            if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
                val user = hashMapOf(
                    "firstName" to firstName,
                    "lastName" to lastName,
                )

                db.collection("users")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(
                            applicationContext,
                            "Record insert with ID: ${documentReference.id}",
                            Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            applicationContext,
                            "Error: " + e.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please insert value first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnDelete!!.setOnClickListener {
            var firstName = txtFirstname!!.text.toString()
            var lastName = txtLastname!!.text.toString()
            if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
                val query = db.collection("users")
                    .whereEqualTo("firstName",firstName)
                    .whereEqualTo("lastName",lastName)
                    .get()
                query.addOnSuccessListener { result ->
                    for (document in result) {
                        db.collection("users").document(document.id).delete().addOnSuccessListener {
                            Toast.makeText(applicationContext, "User Information Delete", Toast.LENGTH_SHORT).show()
                        }
                    }
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

    private fun displayMessage(title: String, data: String) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle(title)
        builder.setMessage(data)
        builder.setCancelable(true)
        builder.show()
    }
}