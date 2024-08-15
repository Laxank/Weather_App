package com.example.weatherappliction

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class weatherforcast : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_weatherforcast)
        goback()
    }

    private fun goback() {
        val onclick = findViewById<TextView>(R.id.back)
        onclick.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

}