package com.example.consultplus.adapter

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.consultplus.R
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class Doctor(@SerializedName("email") @Expose val email : String,
                  @SerializedName("firstname") @Expose val firstname: String,
                  @SerializedName("lastname") @Expose val lastname: String,
                  @SerializedName("specialite") @Expose val specialties: String,
                  )
class DoctorAdapter(val ItemList: ArrayList<Doctor>, private val onItemclicked: (email :String) -> Unit) :
    RecyclerView.Adapter<DoctorAdapter.ItemViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorAdapter.ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.doctor_layout, parent, false)

        return DoctorAdapter.ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val email = ItemList[position].email
        val firstname = ItemList[position].firstname.capitalize()
        val lastname = ItemList[position].lastname.capitalize()
        val specialties = ItemList[position].specialties
        holder.first.text = firstname
        holder.last.text = lastname
        holder.specialties.text = specialties
        holder.itemView.setOnClickListener{
            onItemclicked(email)
        }
        @RequiresApi(Build.VERSION_CODES.Q)
        holder.circle.background.colorFilter = BlendModeColorFilter((getRandomColor()), BlendMode.SRC_ATOP)


    }
    override fun getItemCount() = ItemList.size
    fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
    class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val first =itemView.findViewById<TextView>(R.id.first)
        val last =itemView.findViewById<TextView>(R.id.last)
        val specialties =itemView.findViewById<TextView>(R.id.specialties)
        val circle =itemView.findViewById<View>(R.id.circle)


    }


}