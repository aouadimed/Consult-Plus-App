package com.example.consultplus.view.ui.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.consultplus.GetImg
import com.example.consultplus.adapter.*
import com.example.consultplus.databinding.FragmentBookingBinding
import com.example.consultplus.model.User
import com.example.consultplus.retrofit.CheckTime
import com.example.consultplus.retrofit.Request
import com.example.consultplus.retrofit.Retrofit
import com.example.consultplus.view.ui.activity.preferences
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class BookingFragment : Fragment() {

    internal var emailuser: String? = null
    private  var user = User()
    private var timeSlotsListPosition: Int = 0
    lateinit var DateAdapter: DateAdapter
    private val TimeAdapter by lazy { TimeAdapter() }
     var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var dateList: MutableList<DateModel> = ArrayList()
    private var timeList: MutableList<TimeModel> = ArrayList()
    private  var timeCheck:  MutableList<CheckTime> = ArrayList()
    private val selectedDateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

    private var selectedBookDate: String = ""
    private var selectedBookTime: String = ""
    private var iduser: String = ""



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
        val email = arguments?.getString(BookingFragment.email)

        binding.firstname.setText(firstname)
        binding.lastname.setText(lastname)
        binding.specialties.setText(specialite)

        val retrofit: retrofit2.Retrofit = Retrofit.getInstance()
        val service: Request = retrofit.create(Request::class.java)

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
                    iduser= response.body()?.getId().toString()
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                    println("Message :" + response.errorBody()?.string())

                } }
        }

        //////////TimeAdapter function
        fun TimeAdapterdesplay(timeCheck:  MutableList<String> = ArrayList()){
            var check: Boolean = false
            timeList.clear()
            var time:Int = 8
            for(i in 0 until 12){
                var timeString:String = ""
                if(time<10){
                    timeString = "0$time:00"
                }else{
                    timeString = "$time:00"
                }
                check =  timeString in  timeCheck
                val timeModel = TimeModel(time, selected = false,check=check)
                timeList.add(timeModel)
                time += 1
                TimeAdapter.notifyItemChanged(i)
            }
        }
        ////il mois

////////////DateAdapter
        for (i in 0 until 356) {
            val calendar: Calendar = GregorianCalendar()
            calendar.add(Calendar.DATE, i)
            val dateModel = DateModel(dateFormat.format(calendar.time).toString(), false)
            dateList.add(dateModel)
        }
        DateAdapter = DateAdapter(dateList){date ->
            var timeCheckList:  MutableList<String> = ArrayList()
            timeCheckList =Gettime(doctorId!!,date).toString().replace("CheckTime(time=","")?.replace("[","")?.replace("]","")?.replace(")","")?.replace("\\s+".toRegex(),"")?.split(",") as MutableList<String>;
           if(!Gettime(doctorId!!,date).toString().equals("[]")){
               TimeAdapterdesplay(timeCheckList)
           }else{
               timeList.clear()
               var time:Int = 8
               for(i in 0 until 12){
                   val timeModel = TimeModel(time, selected = false,check= false)
                   timeList.add(timeModel)
                   time += 1
                   TimeAdapter.notifyItemChanged(i)
               }
               TimeAdapter.setData(timeList)
           }
            selectedBookDate=date
        }
        binding.bookDateRv.adapter = DateAdapter

        binding.bookDateRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
///////////time adapter
        var time:Int = 8
        for(i in 0 until 12){
            val timeModel = TimeModel(time, selected = false,check= false)
            timeList.add(timeModel)
            time += 1
        }

        TimeAdapter.setData(timeList)

        binding.bookingTimeSlotRv.adapter = TimeAdapter
        binding.bookingTimeSlotRv.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
        }

        /////////////////////save booking
fun Book(day: String, time: String) {
    // Create JSON using JSONObject
    val jsonObject = JSONObject()
    jsonObject.put("patient",iduser)
    jsonObject.put("doctor", doctorId)
    jsonObject.put("date", day)
    jsonObject.put("time", time)
    jsonObject.put("statu", 0)
    // Convert JSONObject to String
    val jsonObjectString = jsonObject.toString()
    // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
    val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
    CoroutineScope(Dispatchers.IO).launch {
        // Do the POST request and get response
        val response = service.Book(requestBody)
        try {
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(JsonParser.parseString(response.body()?.string()))
                    Log.d("Pretty Printed JSON :", prettyJson)
                    Toast.makeText(context, "booked successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            println(e.printStackTrace())
            println("Error")
        }
    }
}

        TimeAdapter.setOnTimeSelectedListener{ data, position ->
            selectedBookTime  = data
            timeSlotsListPosition = position
        }

        binding.book.setOnClickListener(){
            if(!selectedBookDate.isNullOrEmpty() && !selectedBookTime.isNullOrEmpty() )
            {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to book at this date "+selectedBookDate+" at "+selectedBookTime+" ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->

                        Book(selectedBookDate,selectedBookTime)


                      /*  val dtStart = selectedBookDate+'T'+selectedBookTime
                        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
                        val date = format.parse(dtStart)
                        println(date)
                        Toast.makeText(context,   date.toString(), Toast.LENGTH_SHORT).show()*/

                    }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }else{
                Toast.makeText(context,  "You must pick the day and date", Toast.LENGTH_SHORT).show()
            }
        }

        val dateScroller = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val myLayoutManager: LinearLayoutManager =
                    binding.bookDateRv.layoutManager as LinearLayoutManager
                val scrollPosition = myLayoutManager.findFirstVisibleItemPosition()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val monthYearFormat = SimpleDateFormat("MMM, yyyy", Locale.getDefault())
                val dateParsed = dateFormat.parse(dateList[scrollPosition + 1].date)
                val newMonthYear = monthYearFormat.format(dateParsed)
                binding.monthLbl.text = newMonthYear
            }
        }

        binding.bookDateRv.addOnScrollListener(dateScroller)


        return view
    }

    fun Gettime(doctorid: String, date: String): MutableList<CheckTime> {
        val retrofit: retrofit2.Retrofit = Retrofit.getInstance()
        val service: Request = retrofit.create(Request::class.java)

        val call: Call<List<CheckTime>> = service.Gettimes(doctorid, date)

        call.enqueue(object : Callback<List<CheckTime>> {
            override fun onResponse(
                call: Call<List<CheckTime>>,
                response: Response<List<CheckTime>>
            ) {

                timeCheck = response.body()?.let { ArrayList<CheckTime>(it) }!!
            }

            override fun onFailure(call: Call<List<CheckTime>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

        return timeCheck
    }
    companion object {
        private const val firstname = "firstname"
        private const val lastname = "lastname"
        private const val specialite = "specialite"
        private const val doctorId = "doctorId"
        private const val email = "email"

        // Create `SecondFragment` with bundle so you can send data from certain fragment to `SecondFragment`.
        fun newInstance(Firstname: String,Lastname: String,Specialite: String,DoctorId: String,Email :String) : BookingFragment = BookingFragment().apply {
            val bundle = bundleOf(firstname to Firstname,lastname to Lastname, specialite to Specialite,
                doctorId to DoctorId,email to Email)
            arguments = bundle

        }}

}