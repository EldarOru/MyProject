package com.example.myproject

import android.R.attr.password
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private var fireBaseUserID: String = ""
    lateinit var loginEmail: EditText
    lateinit var loginPassword: EditText
    lateinit var loginButton: Button
    lateinit var registerActivityButton: Button
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "com.example.myproject"
    private val description = "Test notification"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        loginButton = findViewById(R.id.loginButton)
        loginEmail = findViewById(R.id.loginEmail)
        loginPassword = findViewById(R.id.loginPassword)
        registerActivityButton = findViewById(R.id.registerButton)
        loginButton.setOnClickListener {
            createNotification()
            //Toast.makeText(this, "Пиздец...", Toast.LENGTH_SHORT).show()
            mAuth!!.signInWithEmailAndPassword(loginEmail.text.toString(), loginPassword.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val mainIntent = Intent(this, HomeActivity::class.java)
                            fireBaseUserID = mAuth.currentUser!!.uid
                            mainIntent.putExtra("userID",fireBaseUserID)
                            startActivity(mainIntent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        }
        registerActivityButton.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }

        
    }

private fun createNotification(){
    notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val intent = Intent(this, LoginActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        notificationChannel.enableVibration(false)
        notificationManager.createNotificationChannel(notificationChannel)
        builder = Notification.Builder(this, channelId)
                .setContentTitle("CodeAndroid")
                .setContentText("Hello")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
    }else{
        builder = Notification.Builder(this)
                .setContentTitle("CodeAndroid")
                .setContentText("Hello")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
    }
    notificationManager.notify(1234, builder.build())
}
}