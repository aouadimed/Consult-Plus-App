package com.example.consultplus.view.ui.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.consultplus.R
import com.example.consultplus.adapter.*
import com.example.consultplus.databinding.FragmentBookingBinding
import com.example.consultplus.model.User
import com.example.consultplus.retrofit.Request
import com.example.consultplus.retrofit.Retrofit
import com.example.consultplus.view.ui.activity.preferences
import com.google.android.material.internal.ContextUtils
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class BookingFragment : Fragment() {

    internal var email: String? = null
    private  var user = User()
    lateinit var DateAdapter: DateAdapter
    lateinit var TimeAdapter: TimeAdapter
     var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var dateList: MutableList<DateModel> = ArrayList()
    private var timeList: MutableList<TimeModel> = ArrayList()
    private var timeCheck:  MutableList<String> = ArrayList()
    private val selectedDateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

    private var selectedBookDate: String = ""
    private var selectedBookTime: String = ""
    private var Booktime: String = ""

    internal var check: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentBookingBinding.inflate(inflater, container, false)
        val view = binding.root
        val firstname = arguments?.getString(BookingFragment.firstname)
        val lastname = arguments?.getString(BookingFragment.lastname)
        val specialite = arguments?.getString(BookingFragment.specialite)
        val doctorId = arguments?.getString(BookingFragment.doctorId)

        binding.firstname.setText(firstname)
        binding.lastname.setText(lastname)
        binding.specialties.setText(specialite)

        val retrofit: retrofit2.Retrofit = Retrofit.getInstance()
        val service: Request = retrofit.create(Request::class.java)

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

////////////DateAdapter
        for (i in 0 until 31) {
            val calendar: Calendar = GregorianCalendar()
            calendar.add(Calendar.DATE, i)
            val dateModel = DateModel(dateFormat.format(calendar.time).toString(), false)
            dateList.add(dateModel)
        }
        DateAdapter = DateAdapter(dateList){date ->
            selectedBookDate=date
        }
        binding.bookDateRv.adapter = DateAdapter

        binding.bookDateRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
//////////TimeAdapter
        var time:Int = 8
        timeCheck.add("08:00")
        timeCheck.add("09:00")
        timeCheck.add("10:00")
        timeCheck.add("15:00")
        for(i in 0 until 12){
            var timeString:String = ""
            if(time<10){
                timeString = "0$time:00"
            }else{
                timeString = "$time:00"
            }
            println(timeCheck)
            check =  timeString in  timeCheck

            val timeModel = TimeModel(time, selected = false,check=check)
            timeList.add(timeModel)
            time += 1
        }
        TimeAdapter = TimeAdapter(timeList){time->
            Toast.makeText(context, time, Toast.LENGTH_SHORT).show()
            selectedBookTime=time
        }
        binding.bookingTimeSlotRv.adapter = TimeAdapter
        binding.bookingTimeSlotRv.apply {

            layoutManager = GridLayoutManager(requireContext(), 3)

        }



        binding.book.setOnClickListener(){
            val dtStart = selectedBookDate+'T'+selectedBookTime
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
            val date = format.parse(dtStart)
            println(date)
            Toast.makeText(context,   date.toString(), Toast.LENGTH_SHORT).show()
        }






        return view
    }
    companion object {
        private const val firstname = "firstname"
        private const val lastname = "lastname"
        private const val specialite = "specialite"
        private const val doctorId = "DoctorId"

        // Create `SecondFragment` with bundle so you can send data from certain fragment to `SecondFragment`.
        fun newInstance(Firstname: String,Lastname: String,Specialite: String,DoctorId: String) : BookingFragment = BookingFragment().apply {
            val bundle = bundleOf(firstname to Firstname,lastname to Lastname, specialite to Specialite,
                doctorId to DoctorId)
            arguments = bundle

        }}

}