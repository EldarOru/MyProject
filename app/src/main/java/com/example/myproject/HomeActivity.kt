package com.example.myproject

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeActivity : AppCompatActivity() {
    private var fireBaseUserID: String = ""
    lateinit var user: HashMap<String, Any>
    lateinit var userNameText: TextView
    var database = FirebaseDatabase.getInstance().getReference()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        userNameText = findViewById(R.id.nameTextView)
        var intent = getIntent()
        val menuListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue() as HashMap<String, Any>
                userNameText.setText(user.get("name").toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        database.child("users").child(intent.getStringExtra("userID")).addValueEventListener(menuListener)
        }
    }
