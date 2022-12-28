package com.example.consultplus.view.ui.fragment

import PatientAppointmentAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.consultplus.GetImg
import com.example.consultplus.R
import com.example.consultplus.adapter.Doctor
import com.example.consultplus.databinding.FragmentMenuBinding
import com.example.consultplus.databinding.FragmentPatieentAppointmentsBinding
import com.example.consultplus.model.PatientBooking
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
import java.util.*
import kotlin.collections.ArrayList


class PatieentAppointmentsFragment : Fragment() {
    private  var user = User()
    lateinit var recylceTestAdapter: PatientAppointmentAdapter
    internal var patientID: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentPatieentAppointmentsBinding.inflate(inflater, container, false)
        val view = binding.root
        var booklist: ArrayList<PatientBooking> = ArrayList()
        var filteredList = ArrayList<PatientBooking>()
        preferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        email = preferences.getString("EmailUser","")
        GetImg.Image(requireContext(),email!!,binding.imgProfil)
        role = preferences.getString("role","")
        patientID = preferences.getString("ID","")
        user.setEmail(email)
        // Create JSON using JSONObject
        val retrofit: retrofit2.Retrofit = Retrofit.getInstance()

        val service: Request = retrofit.create(Request::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.GetUser(user)
            withContext(Dispatchers.Main) {

                if (response.isSuccessful) {
                    binding.fullName.setText(response.body()?.getLast())
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                    println("Message :" + response.errorBody()?.string())

                } }
        }


        fun displayMyAppointments(){

            val call: Call<List<PatientBooking>> = service.getAppointmentsBypatient(patientID!!)

            call.enqueue(object : Callback<List<PatientBooking>> {
                override fun onResponse(
                    call: Call<List<PatientBooking>>,
                    response: Response<List<PatientBooking>>
                ) {

                    booklist = ArrayList<PatientBooking>(response.body()!!)
                    recylceTestAdapter = PatientAppointmentAdapter(booklist)
                    binding.testRecyclerView.adapter =  recylceTestAdapter
                }

                override fun onFailure(call: Call<List<PatientBooking>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }



        displayMyAppointments()
        binding.testRecyclerView.layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL ,false)








        fun filterList(query: String?) : ArrayList<PatientBooking>{
            filteredList.clear()
            if (query != null) {
                for (i in booklist) {
                    if (i.status.toString() == query ||
                        i.doctor.firstname.lowercase(Locale.ROOT).contains(query)
                        || i.doctor.lastname.lowercase(Locale.ROOT).contains(query)

                    ) {
                        filteredList.add(i)

                    }
                }

                if (filteredList.isEmpty()) {
                    Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show()
                }
            }

            return filteredList
        }
        binding.pending.setOnClickListener {

            recylceTestAdapter.setFilteredList(filterList("0"))

        }
        binding.confiremed.setOnClickListener {
            recylceTestAdapter.setFilteredList(filterList("1"))
        }
        binding.finished.setOnClickListener {
            recylceTestAdapter.setFilteredList(filterList("2"))
        }
        binding.all.setOnClickListener {
            recylceTestAdapter.setFilteredList(booklist)
        }

        binding.searshbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                recylceTestAdapter.setFilteredList(filterList(query))
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                recylceTestAdapter.setFilteredList(filterList(newText))
                return true
            }

        })






        return  view

    }


}