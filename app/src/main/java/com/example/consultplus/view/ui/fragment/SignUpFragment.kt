package com.example.consultplus.view.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.consultplus.R
import com.example.consultplus.retrofit.Request
import com.example.consultplus.retrofit.Retrofit
import com.example.consultplus.view.ui.activity.MainActivity
import com.example.consultplus.view.ui.activity.preferences
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject



class SignUpFragment : Fragment() {
    private lateinit var tvSignIn: TextView
    private lateinit var btnSignUp: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        val email: EditText = view.findViewById(R.id.etEmail)
        val fullname: EditText = view.findViewById(R.id.etFullName)
        val password: EditText = view.findViewById(R.id.etPassword)
        preferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        fun ServiceSignuP(name:String,email:String,password:String) {
            // Create Retrofit
            val retrofit: retrofit2.Retrofit = Retrofit.getInstance()
            val service: Request = retrofit.create(Request::class.java)
            // Create JSON using JSONObject
            val jsonObject = JSONObject()
            jsonObject.put("name", name)
            jsonObject.put("email", email)
            jsonObject.put("password", password)



            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()
            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
            CoroutineScope(Dispatchers.IO).launch {
                // Do the POST request and get response
                val response = service.Signup(requestBody)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Convert raw JSON to pretty JSON using GSON library
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val prettyJson = gson.toJson(JsonParser.parseString(response.body()?.string()))
                        Log.d("Pretty Printed JSON :", prettyJson)
                        Toast.makeText(context, "User ajouter", Toast.LENGTH_SHORT).show()
                        val editor: SharedPreferences.Editor = preferences.edit()
                        editor.putString("EmailUser",email)
                        editor.putBoolean("isloged",true)
                        editor.apply()  //Save Data
                        GoToHome()

                    } else {
                        Log.e("RETROFIT_ERROR", response.code().toString())
                        preferences.edit().clear().apply()
                    }
                }
            }
        }
        tvSignIn = view.findViewById(R.id.tvSignIn)

        btnSignUp = view.findViewById(R.id.button)
        tvSignIn.setOnClickListener {
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragment2, SignInFragment())
            transaction.addToBackStack(null)
            transaction
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
        }

        btnSignUp.setOnClickListener{
            ServiceSignuP(fullname.text.toString().trim(),email.text.toString().trim(),password.text.toString().trim())
        }







    return view
    }
    fun GoToHome() {

        val thisActivity: Activity? = activity
        if (thisActivity != null) {
            startActivity(Intent(thisActivity, MainActivity::class.java)) // if needed
            thisActivity.finish()
        }
    }
}