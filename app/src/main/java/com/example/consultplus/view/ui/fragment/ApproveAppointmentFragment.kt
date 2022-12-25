package com.example.consultplus.view.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consultplus.GetImg
import com.example.consultplus.R
import com.example.consultplus.adapter.Doctor
import com.example.consultplus.adapter.DoctorAppointmentAdapter
import com.example.consultplus.databinding.FragmentApproveAppointmentBinding
import com.example.consultplus.model.Booking
import com.example.consultplus.model.User
import com.example.consultplus.retrofit.Request
import com.example.consultplus.retrofit.Retrofit
import com.example.consultplus.view.ui.activity.preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class ApproveAppointmentFragment : Fragment() {
    internal var doctorID: String? = null
    lateinit var recylcerDoctorAppointmentAdapter: DoctorAppointmentAdapter
    private  var user = User()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentApproveAppointmentBinding.inflate(inflater, container, false)
        val view = binding.root
        preferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        doctorID = preferences.getString("ID","")
        email = preferences.getString("EmailUser","")
        val retrofit: retrofit2.Retrofit = Retrofit.getInstance()
        val service: Request = retrofit.create(Request::class.java)
        user.setEmail(email)
        GetImg.Image(requireContext(),email!!,binding.imgProfil)
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



        var booklist: ArrayList<Booking>
        val call: Call<List<Booking>> = service.getAppointmentsBydoctor(doctorID!!)

        call.enqueue(object : Callback<List<Booking>> {
            override fun onResponse(
                call: Call<List<Booking>>,
                response: Response<List<Booking>>
            ) {

                booklist =  ArrayList<Booking>(response.body())
               recylcerDoctorAppointmentAdapter = DoctorAppointmentAdapter(booklist)
                binding.testRecyclerView.adapter =  recylcerDoctorAppointmentAdapter

            }

            override fun onFailure(call: Call<List<Booking>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })





        binding.testRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)




        return view
    }


}