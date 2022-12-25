package com.example.consultplus.view.ui.activity


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavInflater
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.consultplus.R
import com.example.consultplus.view.ui.fragment.DoctorListFragment
import com.example.consultplus.view.ui.fragment.HomeFragment
import com.example.consultplus.view.ui.fragment.MenuFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


lateinit var preferences: SharedPreferences
private lateinit var bottom_navigation : BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val role = preferences.getString("role","")
        val navHostFragment = (supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment)
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.bottom_nav)
        if (role.isNullOrEmpty()) {
            graph.setStartDestination(R.id.profilUpdateFragment)

        } else {
            graph.setStartDestination(R.id.homeFragment)
        }

        navHostFragment.navController.graph = graph

        bottom_navigation = findViewById(R.id.bottomNavigationView)


        bottom_navigation.itemIconTintList = null;
        bottom_navigation.setOnItemSelectedListener {
           if(role.isNullOrEmpty()){
               Toast.makeText(this@MainActivity, "Complete the process to proceed", Toast.LENGTH_SHORT).show()
           }
               when (it.itemId) {
                   R.id.homeFragment -> {
                       loadFragment(HomeFragment())
                   }
                   R.id.cal -> {
                       loadFragment(DoctorListFragment())
                   }
                   R.id.menu -> {
                      loadFragment(MenuFragment())
                   }
               }





            true

        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment,fragment)
        transaction.addToBackStack("")
        transaction.setReorderingAllowed(true)
        transaction.commit()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

}

