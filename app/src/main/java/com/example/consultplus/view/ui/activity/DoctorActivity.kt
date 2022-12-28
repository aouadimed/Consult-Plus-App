package com.example.consultplus.view.ui.activity


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.NavHostFragment
import com.example.consultplus.R
import com.example.consultplus.view.ui.fragment.ApproveAppointmentFragment
import com.example.consultplus.view.ui.fragment.DoctorListFragment
import com.example.consultplus.view.ui.fragment.HomeFragment
import com.example.consultplus.view.ui.fragment.MenuFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


private lateinit var bottom_navigation : BottomNavigationView

class DoctorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor)
        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val role = preferences.getString("role","")


        bottom_navigation = findViewById(R.id.bottomNavigationView2)

        bottom_navigation.itemIconTintList = null;

        bottom_navigation.setOnItemSelectedListener {

                when (it.itemId) {
                    R.id.Appointment -> { loadFragment(ApproveAppointmentFragment())
                    }
                    R.id.menu2 -> {
                        loadFragment(MenuFragment())
                    }
                }





            true

        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment3,fragment)
        transaction.addToBackStack("")
        transaction.setReorderingAllowed(true)
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_menu_doctor, menu)
        return super.onCreateOptionsMenu(menu)
    }
    }
