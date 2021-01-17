package com.example.myproject

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.properties.Delegates

class Accelerometer(context: Context): SensorEventListener{


    var sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    var sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    override fun onSensorChanged(sensorEvent: SensorEvent?) {
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        TODO("Not yet implemented")
    }
    fun register(){
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_FASTEST)
    }
    fun unregister(){
        sensorManager.unregisterListener(this)
    }
}