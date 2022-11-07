package com.example.consultplus

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
import com.example.consultplus.model.User
import com.example.consultplus.retrofit.Request
import com.example.consultplus.retrofit.retrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit


class SignInFragment : Fragment() {


    private lateinit var btnLogin: Button
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var tvSignUp: TextView
    private lateinit var edtforgot_password: EditText
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
        //  edtforgot_password = view.findViewById(R.id.forget_password)
        tvSignUp = view.findViewById(R.id.tvSignUp)

        fun ServiceLogin(email: String, password: String) {
            // Create Retrofit
            val retrofit: Retrofit = retrofit.getInstance()

            val service: Request = retrofit.create(Request::class.java)
            val User = User()
            User.setEmail(email)
            User.setPassword(password)
            // Create JSON using JSONObject

            CoroutineScope(Dispatchers.IO).launch {
                // Do the POST request and get response
                val response = service.Login(User)
                sharedPreferences =
                    requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
                try {
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
                           //response.body()?.getId()
                            editor.putString("id", response.body()?.getId())
                            editor.putString("NameUser", response.body()?.getFullName())
                            editor.putString("password", password)
                            editor.putString("EmailUser", response.body()?.getEmail())
                            println("Email ====>>>>> " + response.body()?.getEmail())
                            editor.apply()  //Save Data
                            Log.d("ID_USER", "ID_USER : ${response.body()?.getId()}")
                            //  println("Token =============>>>>>>>>>  "+response.body()?.string())
                        //    GoToHome(email)==
                            Toast.makeText(context, "User exist"+response.body()?.getId(), Toast.LENGTH_SHORT).show()

                        } else {
                            Log.e("RETROFIT_ERROR", response.code().toString())
                            println("Message :" + response.errorBody()?.string())
                            sharedPreferences.edit().clear().apply()

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
            transaction.replace(R.id.fragment2, SignUpFragment()).commit()
        }
        return view

    }

    /*  private fun validate(): Boolean {


        if (edtEmail.text!!.isEmpty()){


            return false
        }

        if (edtPassword.text!!.isEmpty()){

            return false
        }

        return true
    }*/
    fun GoToHome(email : String) {
        val intent: Intent = Intent(activity, MainActivity::class.java).apply {
            putExtra("CURRENT_EMAIL",email)
            Log.d("email","$email : from login activity ")

        }
        startActivity(intent)
    }

}