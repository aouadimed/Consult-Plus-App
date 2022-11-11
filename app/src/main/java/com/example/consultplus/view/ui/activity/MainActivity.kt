package com.example.consultplus

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

lateinit var preferences: SharedPreferences

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val email = preferences.getString("EmailUser","")

        Toast.makeText(this, "User exist"+email, Toast.LENGTH_SHORT).show()


    }
}