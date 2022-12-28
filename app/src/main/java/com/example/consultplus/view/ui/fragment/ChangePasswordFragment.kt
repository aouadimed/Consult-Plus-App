package com.example.consultplus.view.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import com.example.consultplus.R
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


class ChangePasswordFragment : Fragment() {
    private lateinit var btnSignUp: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_change_password, container, false)
        val token = arguments?.getString(Token)
        val password: EditText = view.findViewById(R.id.etPassword)
        val password2: EditText = view.findViewById(R.id.password2)
        btnSignUp = view.findViewById(R.id.button)

        fun resetPassword(password : String,password2 : String) {
            // Create Retrofit
            val retrofit: retrofit2.Retrofit = Retrofit.getInstance()

            val service: Request = retrofit.create(Request::class.java)

            // Create JSON using JSONObject
            val jsonObject = JSONObject()
            jsonObject.put("password", password)
            jsonObject.put("confirmPassword", password2)
            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()
            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
            CoroutineScope(Dispatchers.IO).launch {
                // Do the POST request and get response
                val response = service.resetPassword(requestBody)
                try {
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val gson = GsonBuilder().setPrettyPrinting().create()
                            val prettyJson = gson.toJson(JsonParser.parseString(response.body()?.string()))
                            Toast.makeText(context, prettyJson, Toast.LENGTH_SHORT).show()
                            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                            transaction.replace(R.id.fragment2, SignInFragment())
                            transaction.addToBackStack(null)
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            transaction.commit()
                        } else {
                            Toast.makeText(context, response.message().toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    println(e.printStackTrace())
                    println("Error")
                }
            }
        }


        btnSignUp.setOnClickListener(){
            resetPassword(password.text.toString(),password2.text.toString())
        }






        return view
    }
    companion object {
        private const val Token = "Token"


        // Create `SecondFragment` with bundle so you can send data from certain fragment to `SecondFragment`.
        fun newInstance(token: String) : ChangePasswordFragment = ChangePasswordFragment().apply {
            val bundle = bundleOf(Token to token)
            arguments = bundle

        }}
}