package com.example.consultplus.view.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.consultplus.R
import com.example.consultplus.databinding.FragmentForgetPasswordBinding
import com.example.consultplus.retrofit.Request
import com.example.consultplus.retrofit.Retrofit
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


class ForgetPasswordFragment : Fragment() {

    lateinit var binding: FragmentForgetPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =FragmentForgetPasswordBinding.inflate(inflater, container, false)
        val view = binding.root



        fun sendlink(email : String) {
            // Create Retrofit
            val retrofit: retrofit2.Retrofit = Retrofit.getInstance()

            val service: Request = retrofit.create(Request::class.java)

            // Create JSON using JSONObject
            val jsonObject = JSONObject()
            jsonObject.put("email", email)
            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()
            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
            CoroutineScope(Dispatchers.IO).launch {
                // Do the POST request and get response
                val response = service.getemail(requestBody)
                try {
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val gson = GsonBuilder().setPrettyPrinting().create()
                            val prettyJson = gson.toJson(JsonParser.parseString(response.body()?.string()))
                            Toast.makeText(context, "Please check your email", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "User dosen't exist", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    println(e.printStackTrace())
                    println("Error")
                }
            }
        }

        binding.btSend.setOnClickListener(){
            sendlink(binding.etEmail.text.toString().toLowerCase())
        }

        binding.tvSignUp.setOnClickListener(){
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragment2, SignUpFragment()).addToBackStack("").commit()
        }

        return view
    }


}