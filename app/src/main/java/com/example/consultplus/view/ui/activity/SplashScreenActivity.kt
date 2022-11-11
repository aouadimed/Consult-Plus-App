package com.example.consultplus

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val email = preferences.getString("EmailUser","")
        Handler().postDelayed({
            if (email != null) {
                val intent = Intent(this@SplashScreenActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else {
                val intent = Intent(this@SplashScreenActivity,AuthActivity::class.java)
                startActivity(intent)
                finish()
            }
        },2999)






        }
    }

