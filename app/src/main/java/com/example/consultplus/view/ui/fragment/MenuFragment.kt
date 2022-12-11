package com.example.consultplus.view.ui.fragment

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.consultplus.R
import com.example.consultplus.databinding.FragmentMenuBinding
import com.example.consultplus.view.ui.activity.AuthActivity

import com.example.consultplus.view.ui.activity.preferences

class MenuFragment : Fragment() {


    lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentMenuBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.logout.setOnClickListener(){
            preferences = requireActivity().getSharedPreferences("SHARED_PREF", AppCompatActivity.MODE_PRIVATE)
            preferences.edit().clear().apply()
            val thisActivity: Activity? = activity
            if (thisActivity != null) {
                startActivity(Intent(thisActivity, AuthActivity::class.java)) // if needed
                thisActivity.finish()
            }
        }
        binding.profil.setOnClickListener(){
            loadFragment(ProfilUpdateFragment())
        }

        return view
    }
    private  fun loadFragment(fragment: Fragment){
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment, fragment).addToBackStack("").commit()
    }


}