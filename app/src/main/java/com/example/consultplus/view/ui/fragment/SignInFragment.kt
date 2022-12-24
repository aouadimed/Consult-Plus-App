package com.example.consultplus.view.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.consultplus.R
import com.example.consultplus.view.ui.activity.MainActivity
import com.example.consultplus.model.User
import com.example.consultplus.retrofit.Request
import com.example.consultplus.retrofit.Retrofit
import com.example.consultplus.view.ui.activity.DoctorActivity
import com.example.consultplus.view.ui.activity.preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class SignInFragment : Fragment() {

    private lateinit var btnLogin: Button
    private lateinit var tvSignUp: TextView
    private lateinit var edtforgot_password: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)


        val email: EditText = view.findViewById(R.id.etEmail)
        val password: EditText = view.findViewById(R.id.etPassword)

        btnLogin = view.findViewById(R.id.bt_sign_in)
        edtforgot_password = view.findViewById(R.id.forget_password)
        tvSignUp = view.findViewById(R.id.tvSignUp)
        edtforgot_password.setOnClickListener(){
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragment2, ForgetPasswordFragment()).addToBackStack("").commit()
        }
        fun ServiceLogin(email: String, password: String) {
            // Create Retrofit
            val retrofit: retrofit2.Retrofit = Retrofit.getInstance()

            val service: Request = retrofit.create(Request::class.java)
            val User = User()
            User.setEmail(email)
            User.setPassword(password)
            // Create JSON using JSONObject

            CoroutineScope(Dispatchers.IO).launch {
                // Do the POST request and get response
                val response = service.Login(User)
                preferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
                try {
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val editor: SharedPreferences.Editor = preferences.edit()
                            editor.putString("EmailUser", response.body()?.getEmail())
                            editor.putString("ID", response.body()?.getId())
                            editor.putString("role", response.body()?.getRole())
                            editor.putBoolean("isloged",true)

                            println("Email ====>>>>> " + response.body()?.getEmail())
                            editor.apply()  //Save Data
                            Log.d("ID_USER", "ID_USER : ${response.body()?.getEmail()}")
                            //  println("Token =============>>>>>>>>>  "+response.body()?.string())

                            Toast.makeText(context, "User exist"+response.body()?.getEmail(), Toast.LENGTH_SHORT).show()
                            if( response.body()?.getRole().equals("patient")){
                                GoToHome()
                            }else{
                                GoToBack()
                            }

                        } else {
                            Log.e("RETROFIT_ERROR", response.code().toString())
                            println("Message :" + response.errorBody()?.string())
                            preferences.edit().clear().apply()

                        }
                    }
                } catch (e: Exception) {
                    println(e.printStackTrace())
                    println("Error")
                }
            }
        }





        btnLogin.setOnClickListener {
            ServiceLogin(email.text.toString().trim(),password.text.toString().trim())
        }






        tvSignUp.setOnClickListener {
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragment2, SignUpFragment()).addToBackStack("").commit()
        }
        return view

    }

    fun GoToBack() {

        val thisActivity: Activity? = activity
        if (thisActivity != null) {
            startActivity(Intent(thisActivity, DoctorActivity::class.java)) // if needed
            thisActivity.finish()
        }
    }
    fun GoToHome() {

            val thisActivity: Activity? = activity
            if (thisActivity != null) {
                startActivity(Intent(thisActivity, MainActivity::class.java)) // if needed
                thisActivity.finish()
            }
    }

}