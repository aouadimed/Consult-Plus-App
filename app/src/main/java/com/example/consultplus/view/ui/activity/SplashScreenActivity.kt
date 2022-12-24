package com.example.consultplus.view.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.consultplus.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val isloged = preferences.getBoolean("isloged",false)
        val role = preferences.getString("role","")
        Handler().postDelayed({
            if (isloged) {
                if(role.equals("doctor")){
                    val intent = Intent(this@SplashScreenActivity, DoctorActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }else {
                val intent = Intent(this@SplashScreenActivity, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }
        },2999)






        }
    }

