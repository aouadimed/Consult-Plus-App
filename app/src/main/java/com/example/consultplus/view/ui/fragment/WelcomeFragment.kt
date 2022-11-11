package com.example.consultplus.view.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import com.example.consultplus.R


class WelcomeFragment : Fragment() {
    private lateinit var btnSignUp: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)


        btnSignUp = view.findViewById(R.id.button)
        btnSignUp .setOnClickListener {
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragment2, SignInFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }


}