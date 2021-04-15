package com.example.myproject

import android.content.Context
import android.content.Intent.getIntent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeActivity : AppCompatActivity(), SensorEventListener {
    lateinit var user: HashMap<String, Any>
    lateinit var userNameText: TextView
    lateinit var accelerometerTextView: TextView
    lateinit var playSoundButton: Button
    //lateinit var yameteSound: MediaPlayer
    var yameteSound: MediaPlayer? = null
    lateinit var sensorManager: SensorManager
    lateinit var audioManager: AudioManager
    //lateinit var accelerometer: Accelerometer

    var database = FirebaseDatabase.getInstance().getReference()
    override fun onSensorChanged(sensor: SensorEvent?) {
        accelerometerTextView.setText(sensor!!.values[0].toString())
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        playSoundButton = findViewById(R.id.playSoundButton)
        userNameText = findViewById(R.id.nameTextView)
        var intent = getIntent()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 10,0)
        accelerometerTextView = findViewById(R.id.accelerometerTextView)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        //accelerometer = Accelerometer(this)

        val menuListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue() as HashMap<String, Any>
                userNameText.setText("Hello " + user.get("name").toString() + " !")
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }

        database.child("users").child(intent.getStringExtra("userID")).addValueEventListener(menuListener)

        playSoundButton.setOnClickListener {
            if (yameteSound == null) {
                yameteSound = MediaPlayer.create(this, R.raw.yamete)
                yameteSound?.start()
            }else{
                yameteSound?.release()
                yameteSound = null
            }





        }
        }
    }
