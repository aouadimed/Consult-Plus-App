package com.example.consultplus.view.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.consultplus.R
import com.example.consultplus.view.ui.fragment.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

lateinit var preferences: SharedPreferences
private lateinit var bottom_navigation : BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var bottomNav : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bottom_navigation = findViewById(R.id.bottomNavigationView)
        bottom_navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    loadFragment(HomeFragment())
                }
                R.id.sign_out -> {
                    preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
                    preferences.edit().clear().apply()
                    val intent = Intent(this,AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            true

        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment,fragment)
        transaction.addToBackStack(null)

        transaction.commit()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.sign_out){



        }

        return super.onOptionsItemSelected(item)
    }

}

