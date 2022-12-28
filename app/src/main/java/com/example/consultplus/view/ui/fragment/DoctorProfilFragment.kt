package com.example.consultplus.view.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import com.example.consultplus.GetImg
import com.example.consultplus.R
import com.example.consultplus.databinding.FragmentDoctorProfilBinding
import com.example.consultplus.model.User
import com.example.consultplus.retrofit.Request
import com.example.consultplus.retrofit.Retrofit
import com.example.consultplus.view.ui.activity.preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DoctorProfilFragment : Fragment() {
    private var DoctorId: String = ""
    internal var emailuser: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentDoctorProfilBinding.inflate(inflater, container, false)
        val view = binding.root
        val retrofit: retrofit2.Retrofit = Retrofit.getInstance()
        val service: Request = retrofit.create(Request::class.java)
        val email = arguments?.getString(DoctorProfilFragment.email)
        var user = User()
        preferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        emailuser = preferences.getString("EmailUser","")
        GetImg.Image(requireContext(),emailuser!!,binding.imgProfil)
        GetImg.Image(requireContext(),email!!,binding.img)
        user.setEmail(emailuser)
        // Create JSON using JSONObject

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.GetUser(user)
            withContext(Dispatchers.Main) {

                if (response.isSuccessful) {
                    user.setFullname(response.body()?.getFullname())
                    binding.fullName.text = user.getFullname()+" !"
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                    println("Message :" + response.errorBody()?.string())

                } }
        }

        fun displayDocotr(){
            var user = User()
            user.setEmail(email)

            CoroutineScope(Dispatchers.IO).launch {
                // Do the POST request and get response
                val response = service.GetDoctor(user)
                withContext(Dispatchers.Main) {

                    if (response.isSuccessful) {
                        DoctorId= response.body()?.getId().toString()
                            binding.firstname.setText(response.body()?.getFirst()!!.capitalize())
                            binding.lastname.setText(response.body()?.getLast()!!.capitalize())
                          //  binding.adresse.setText(response.body()?.getAdresse())
                                binding.experience.setText(response.body()?.getex())
                                binding.specialties.setText(response.body()?.getS())
                                binding.numpatient.setText(response.body()?.getpatient())
                                binding.description.setText(response.body()?.getdes())
                    } else {
                        Log.e("RETROFIT_ERROR", response.code().toString())
                        println("Message :" + response.errorBody()?.string())

                    } }
            }

        }




        displayDocotr()

        binding.button.setOnClickListener{
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragment,BookingFragment.newInstance(binding.firstname.text.toString(),binding.lastname.text.toString(),binding.specialties.text.toString(),DoctorId,email)).addToBackStack("")
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
            println(DoctorId)
        }


        return view
    }






    companion object {
        private const val email = "email"


        // Create `SecondFragment` with bundle so you can send data from certain fragment to `SecondFragment`.
        fun newInstance(Email: String) : DoctorProfilFragment = DoctorProfilFragment().apply {
            val bundle = bundleOf(email to Email)
            arguments = bundle

        }}
}