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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.consultplus.GetImg
import com.example.consultplus.R
import com.example.consultplus.databinding.FragmentMenuBinding
import com.example.consultplus.model.User
import com.example.consultplus.retrofit.Request
import com.example.consultplus.retrofit.Retrofit
import com.example.consultplus.view.ui.activity.AuthActivity

import com.example.consultplus.view.ui.activity.preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
private  var user = User()
class MenuFragment : Fragment() {


    lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentMenuBinding.inflate(inflater, container, false)
        val view = binding.root

        preferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        email = preferences.getString("EmailUser","")
        GetImg.Image(requireContext(),email!!,binding.imgProfil)
        role = preferences.getString("role","")
        user.setEmail(email)
        // Create JSON using JSONObject
        val retrofit: retrofit2.Retrofit = Retrofit.getInstance()

        val service: Request = retrofit.create(Request::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.GetUser(user)
            withContext(Dispatchers.Main) {

                if (response.isSuccessful) {
                        binding.email.setText(response.body()?.getEmail())
                        binding.firstname.setText(response.body()?.getFirst())
                        binding.lastname.setText(response.body()?.getLast())
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                    println("Message :" + response.errorBody()?.string())

                } }
        }







       binding.notification.setOnClickListener{

            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragment, PatieentAppointmentsFragment()).addToBackStack("").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()


        }


        if (role.equals("doctor")){
            binding.notification.visibility = View.GONE
        }



        binding.logout.setOnClickListener(){
            preferences = requireActivity().getSharedPreferences("SHARED_PREF", AppCompatActivity.MODE_PRIVATE)
            context?.let { it1 -> Glide.get(it1).clearMemory() };
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
        if(role.equals("patient")){
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragment, fragment).addToBackStack("").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
        }else{
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragment3, fragment).addToBackStack("").commit()
        }

    }


}