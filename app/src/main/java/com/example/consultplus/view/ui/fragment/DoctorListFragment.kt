package com.example.consultplus.view.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.consultplus.R
import com.example.consultplus.adapter.Doctor
import com.example.consultplus.adapter.DoctorAdapter
import com.example.consultplus.adapter.MostPopularAdapter
import com.example.consultplus.adapter.MyObject
import com.example.consultplus.databinding.FragmentDoctorListBinding
import com.example.consultplus.model.User
import com.example.consultplus.retrofit.Request
import com.example.consultplus.retrofit.Retrofit
import com.example.consultplus.view.ui.activity.preferences
import com.google.android.material.internal.ContextUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DoctorListFragment : Fragment() {
    lateinit var recylcerDoctorAdapter: DoctorAdapter

    lateinit var binding: FragmentDoctorListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDoctorListBinding.inflate(inflater, container, false)
        val view = binding.root

        val specialite = arguments?.getString(DoctorListFragment.specialite)
        val retrofit: retrofit2.Retrofit = Retrofit.getInstance()
        val service: Request = retrofit.create(Request::class.java)

        var user = User()

        preferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        email = preferences.getString("EmailUser","")

        user.setEmail(email)
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

        fun getDoctorsbySp(){

            var boxList: ArrayList<Doctor>

            val call: Call<List<Doctor>>? = specialite?.let { service.getbyspecialite(it) }


            call?.enqueue(object : Callback<List<Doctor>> {
                override fun onResponse(call: Call<List<Doctor>>, response: Response<List<Doctor>>) {
                    boxList =  ArrayList<Doctor>(response.body())
                    recylcerDoctorAdapter = DoctorAdapter(boxList){ email->
                        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                        transaction.replace(R.id.fragment,DoctorProfilFragment.newInstance(email)).addToBackStack("").commit()
                    }
                   binding.testRecyclerView.adapter =   recylcerDoctorAdapter
                }

                override fun onFailure(call: Call<List<Doctor>>, t: Throwable) {
                    TODO("Not yet implemented")
                }


            })

            binding.testRecyclerView.layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL ,false)

        }
        fun allDoctors(){

            var boxList: ArrayList<Doctor>

            val call: Call<List<Doctor>>? = service.getAllDoctor()


            call?.enqueue(object : Callback<List<Doctor>> {
                override fun onResponse(call: Call<List<Doctor>>, response: Response<List<Doctor>>) {
                    boxList =  ArrayList<Doctor>(response.body())
                    recylcerDoctorAdapter = DoctorAdapter(boxList){email->
                        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                        transaction.replace(R.id.fragment,DoctorProfilFragment.newInstance(email)).addToBackStack("").commit()
                    }
                    binding.testRecyclerView.adapter =   recylcerDoctorAdapter
                }

                override fun onFailure(call: Call<List<Doctor>>, t: Throwable) {
                    TODO("Not yet implemented")
                }


            })

            binding.testRecyclerView.layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL ,false)

        }

        if(specialite.isNullOrEmpty()){
            allDoctors()
        }else{
            getDoctorsbySp()
        }








        return view
    }




    companion object {
        private const val specialite = "specialite"


        // Create `SecondFragment` with bundle so you can send data from certain fragment to `SecondFragment`.
        fun newInstance(Specialite: String) : DoctorListFragment = DoctorListFragment().apply {
            val bundle = bundleOf(specialite to Specialite)
            arguments = bundle

        }}
}