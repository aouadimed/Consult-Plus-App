package com.example.consultplus.view.ui.fragment


import PatientAppointmentAdapter
import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.consultplus.GetImg
import com.example.consultplus.R
import com.example.consultplus.adapter.*
import com.example.consultplus.model.PatientBooking
import com.example.consultplus.model.User
import com.example.consultplus.retrofit.Request
import com.example.consultplus.retrofit.Retrofit
import com.example.consultplus.view.ui.activity.preferences
import com.google.android.material.internal.ContextUtils
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL


class HomeFragment : Fragment() {

    lateinit var recylcerMostPopularAdapter: MostPopularAdapter
    lateinit var recylcerMostPopular: RecyclerView


    lateinit var recylceTestAdapter: PatientAppointmentAdapter
    lateinit var recylcerTest: RecyclerView
    lateinit var Image: ImageView
    internal var patientID: String? = null

    private lateinit var nameTag: TextView
    private lateinit var test_tile: TextView
    private lateinit var test_tile_2: TextView
    lateinit var sharedPreferences: SharedPreferences
    internal var email: String? = null
    lateinit var expand: View
    private  var user = User()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_home, container, false)

        nameTag = view.findViewById(R.id.full_name)

        val retrofit: retrofit2.Retrofit = Retrofit.getInstance()
        val service: Request = retrofit.create(Request::class.java)

        preferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        email = preferences.getString("EmailUser","")
        patientID = preferences.getString("ID","")

        user.setEmail(email)
        // Create JSON using JSONObject

        Image = view.findViewById(R.id.img_profil)

        GetImg.Image(requireContext(),email!!,Image)


        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.GetUser(user)
            withContext(Dispatchers.Main) {

            if (response.isSuccessful) {
                user.setFullname(response.body()?.getFullname())
                nameTag.text = user.getFullname()+" !"
            } else {
                Log.e("RETROFIT_ERROR", response.code().toString())
                println("Message :" + response.errorBody()?.string())

            } }
        }
        ////most popular
    fun desplayMostPopuler(){
            recylcerMostPopular = view.findViewById(R.id.boxRecyclerView)
            var boxList: ArrayList<MyObject>
            val call: Call<List<MyObject>> = service.mostPopuler()
            call.enqueue(object : Callback<List<MyObject>> {
                override fun onResponse(call: Call<List<MyObject>>, response: Response<List<MyObject>>) {
                    boxList = response.body()?.let { ArrayList<MyObject>(it) }!!
                    recylcerMostPopularAdapter = MostPopularAdapter(boxList){ specialties ->
                        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                        transaction.replace(R.id.fragment,DoctorListFragment.newInstance(specialties)).addToBackStack("")
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit()
                    }
                    recylcerMostPopular.adapter =  recylcerMostPopularAdapter
                }

                override fun onFailure(call: Call<List<MyObject>>, t: Throwable) {
                    ContextUtils.getActivity(context)?.runOnUiThread(java.lang.Runnable {
                    })
                    Log.d("***", "Opppsss" + t.message)
                }

            })
    }




        desplayMostPopuler()
        recylcerMostPopular.layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL ,false)

        ////////testtttt

        fun displayMyAppointments(){
            var booklist: ArrayList<PatientBooking>
            val call: Call<List<PatientBooking>> = service.getAppointmentsBypatient(patientID!!)
            recylcerTest = view.findViewById(R.id.testRecyclerView)
            call.enqueue(object : Callback<List<PatientBooking>> {
                override fun onResponse(
                    call: Call<List<PatientBooking>>,
                    response: Response<List<PatientBooking>>
                ) {

                    booklist = ArrayList<PatientBooking>(response.body()!!)
                    recylceTestAdapter = PatientAppointmentAdapter(booklist.take(4) as MutableList<PatientBooking>)
                    recylcerTest.adapter =  recylceTestAdapter
                }

                override fun onFailure(call: Call<List<PatientBooking>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }



        displayMyAppointments()
        recylcerTest.layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL ,false)

       expand = view.findViewById(R.id.full_popular_2)

        expand.setOnClickListener{

            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragment, PatieentAppointmentsFragment()).addToBackStack("").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()


        }


        return view
    }

 }
