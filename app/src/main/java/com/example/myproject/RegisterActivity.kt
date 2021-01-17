package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.sql.Time
import java.util.*
import kotlin.concurrent.timerTask


class RegisterActivity : AppCompatActivity() {
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-z]+"
    private val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,15}$"
    private lateinit var mAuth: FirebaseAuth
    private var fireBaseUserID: String = ""
    private lateinit var database: DatabaseReference
    lateinit var progressBar: ProgressBar
    lateinit var registerButton: Button
    lateinit var backLoginActivity: Button
    lateinit var nameText: EditText
    lateinit var emailText: EditText
    lateinit var passwordText: EditText
    lateinit var repeatPasswordText: EditText
    lateinit var radioGroup: RadioGroup
    lateinit var radioButton: RadioButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        progressBar = findViewById(R.id.progressBarRegister)
        registerButton = findViewById(R.id.registerButtonOne)
        backLoginActivity = findViewById(R.id.backLoginActivity)
        radioGroup = findViewById(R.id.groupGender)
        nameText = findViewById(R.id.name)
        emailText = findViewById(R.id.email)
        passwordText = findViewById(R.id.password)
        repeatPasswordText = findViewById(R.id.repeatPassword)
        mAuth = FirebaseAuth.getInstance()
        val loginIntent = Intent(this, LoginActivity::class.java)

        backLoginActivity.setOnClickListener {
            startActivity(loginIntent)
        }

        registerButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            var selectedID = radioGroup.checkedRadioButtonId
            var email = emailText.text.toString()
            var name = nameText.text.toString()
            var password = passwordText.text.toString()
            var repeatPassword = repeatPasswordText.text.toString()
            radioButton = findViewById(selectedID)
            if(checkInputData(email, name, password, repeatPassword)){
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                progressBar.visibility = View.GONE
                                mAuth.currentUser!!.sendEmailVerification()
                                fireBaseUserID = mAuth.currentUser!!.uid
                                database = FirebaseDatabase.getInstance().getReference()
                                val user = User(email, name, checkGender(radioButton))
                                database.child("users").child(fireBaseUserID).setValue(user)
                                Toast.makeText(this, "You have successfully registered, check your email for verification",Toast.LENGTH_SHORT).show()
                                Timer("Left", false).schedule(timerTask { startActivity(loginIntent)}, Toast.LENGTH_SHORT.toLong())
                                //работает ли вообще эта функция..?
                                finish()
                            } else {
                                Toast.makeText(this@RegisterActivity, "Error Message: " + task.exception!!.message,
                                        Toast.LENGTH_SHORT).show()
                            }
                        }
            }
        }
    }

    private fun checkInputData(email: String, name: String, password: String, repeatPassword: String):Boolean{
        return if (!email.matches(emailPattern.toRegex())){
            Toast.makeText(this, "Incorrect email", Toast.LENGTH_SHORT).show()
            false
        }else if(name.isEmpty()){
            Toast.makeText(this, "Incorrect name", Toast.LENGTH_SHORT).show()
            false
        }else if(!password.matches(passwordPattern.toRegex())){
            Toast.makeText(this, "Incorrect password. 1 letter, 1 number, more than 8 characters", Toast.LENGTH_SHORT).show()
            false
        }else if(password != repeatPassword){
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            false
        }else true
    }

    private fun checkGender(radioButton: RadioButton): String{
        return if (radioButton.id.toString() == R.id.radioMale.toString()){
            "male"
        }else {
            "female"
        }
    }
}