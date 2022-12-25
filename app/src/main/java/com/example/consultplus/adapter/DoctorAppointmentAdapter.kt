package com.example.consultplus.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.consultplus.R
import com.example.consultplus.R.drawable.bt_rectangle
import com.example.consultplus.R.drawable.bt_supprimer
import com.example.consultplus.model.Booking
import com.example.consultplus.retrofit.Request
import com.example.consultplus.retrofit.Retrofit
import com.example.consultplus.view.ui.fragment.ApproveAppointmentFragment
import com.example.consultplus.view.ui.fragment.SetRole
import com.example.consultplus.view.ui.fragment.email
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.security.AccessController.getContext

class DoctorAppointmentAdapter(val ItemList: MutableList<Booking>) : RecyclerView.Adapter<DoctorAppointmentAdapter.DoctorAppointmentViewHolder>() {
    private lateinit var  context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  DoctorAppointmentAdapter.DoctorAppointmentViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.appointment_layout, parent, false)

        return  DoctorAppointmentAdapter.DoctorAppointmentViewHolder(view)
    }

    override fun getItemCount() = ItemList.size

    class DoctorAppointmentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val firstname =itemView.findViewById<TextView>(R.id.first)
        val lastname =itemView.findViewById<TextView>(R.id.last)
        val bookDate =itemView.findViewById<TextView>(R.id.bookdate)
        val booktime =itemView.findViewById<TextView>(R.id.booktime)
        val button =itemView.findViewById<Button>(R.id.button)


    }

    override fun onBindViewHolder(holder: DoctorAppointmentViewHolder, position: Int) {
        val firstname = ItemList[position].patient.firstname.capitalize()
        val lastname = ItemList[position].patient.lastname.capitalize()
        val bookdate = ItemList[position].date
        val booktime = ItemList[position].time
        var satuts = ItemList[position].status
        val ID = ItemList[position].ID

        holder.firstname.text = firstname
        holder.lastname.text = lastname
        holder.bookDate.text = bookdate
        holder.booktime.text = booktime

        if(satuts == 1){
            holder.button.background = ContextCompat.getDrawable(context, bt_supprimer)
            holder.button.text = "Cancel"
        }else{
            holder.button.background = ContextCompat.getDrawable(context, bt_rectangle)
            holder.button.text = "Confirmer"
        }

        holder.button.setOnClickListener{

            if(holder.button.text.equals("Confirmer")){
                statuschange(ID)
                changeFragment(ApproveAppointmentFragment(),context)
            }else{
                deleteiem(ID)
                changeFragment(ApproveAppointmentFragment(),context)
            }

        }



    }
    fun changeFragment(newFragment: Fragment?, context: Context) {
        val transaction: FragmentTransaction = (context as FragmentActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment3, newFragment!!)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    fun deleteiem(ID: String) : Boolean{
        val retrofit: retrofit2.Retrofit = Retrofit.getInstance()

        val service: Request = retrofit.create(Request::class.java)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("_id",ID)
        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()
        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.DeleteBokking(requestBody)
            try {
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val prettyJson = gson.toJson(JsonParser.parseString(response.body()?.string()))
                        Log.d("Pretty Printed JSON :", prettyJson)
                        return@withContext true
                    } else {
                        return@withContext false
                    }
                }
            } catch (e: Exception) {
                println(e.printStackTrace())
                println("Error")
            }
        }
        return false




    }







    fun statuschange(ID : String) : Boolean {
        // Create Retrofit
        val retrofit: retrofit2.Retrofit = Retrofit.getInstance()

        val service: Request = retrofit.create(Request::class.java)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("_id",ID)
        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()
        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.EditStatus(requestBody)
            try {
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val prettyJson = gson.toJson(JsonParser.parseString(response.body()?.string()))
                        Log.d("Pretty Printed JSON :", prettyJson)
                        return@withContext true
                    } else {
                        return@withContext false
                    }
                }
            } catch (e: Exception) {
                println(e.printStackTrace())
                println("Error")
            }
        }
        return false
    }
}