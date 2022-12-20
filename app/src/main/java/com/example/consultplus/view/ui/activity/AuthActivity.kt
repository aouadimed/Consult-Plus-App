package com.example.consultplus.view.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.consultplus.R
import com.example.consultplus.view.ui.fragment.ChangePasswordFragment

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        if (intent.data?.toString()?.contains("192.168.1.11") == true) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment2,ChangePasswordFragment.newInstance(intent.data?.getQueryParameter("token").toString()))
            transaction.setReorderingAllowed(false)
            transaction.commit()
        }
    }

}