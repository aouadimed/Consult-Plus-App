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
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.consultplus.R
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class MyObject(@DrawableRes val EmojiPic: Int, @SerializedName("_id") @Expose val specialties : String,@SerializedName("count") @Expose val specialties_count: Int)

class MostPopularAdapter(val ItemList: ArrayList<MyObject>) : RecyclerView.Adapter<MostPopularAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.most_popular_layout, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val count = ItemList[position].specialties_count
        val specialties = ItemList[position].specialties

     //   holder.emoji.setImageResource(ItemList[position].EmojiPic)
        holder.count.text = count.toString()
        holder.specialties.text = specialties
        @RequiresApi(Build.VERSION_CODES.Q)
        holder.card.background.colorFilter = BlendModeColorFilter((getRandomColor()), BlendMode.SRC_ATOP)
        if(specialties == "Dermatology"){
            holder.emoji.setImageResource(R.drawable.dermatology)
        }
        if(specialties == "Optometry"){
            holder.emoji.setImageResource(R.drawable.optometry)
        }
        if(specialties == "Neurosurgery"){
            holder.emoji.setImageResource(R.drawable.neurosurgery)
        }
        if(specialties == "General Surgery"){
            holder.emoji.setImageResource(R.drawable.general_surgery)
        }
        if(specialties == "Psychiatry"){
            holder.emoji.setImageResource(R.drawable.psychiatry)
        }
        if(specialties == "Ophthalmology"){
            holder.emoji.setImageResource(R.drawable.ophthalmology)
        }
        if(specialties == "Virology"){
            holder.emoji.setImageResource(R.drawable.virology)
        }
        if(specialties == "Radiology"){
            holder.emoji.setImageResource(R.drawable.radiology)
        }
        if(specialties == "Plastic Surgery"){
            holder.emoji.setImageResource(R.drawable.plastic_surgery)
        }
        if(specialties == "Obstetrics"){
            holder.emoji.setImageResource(R.drawable.obstetrics)
        }
        if(specialties == "Orthopedics"){
            holder.emoji.setImageResource(R.drawable.orthopedics)
        }



    }
    fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
    override fun getItemCount() = ItemList.size

    class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val count =itemView.findViewById<TextView>(R.id.specialties_count)
        val specialties =itemView.findViewById<TextView>(R.id.specialties)
        val emoji =itemView.findViewById<ImageView>(R.id.emoji)
        val card =itemView.findViewById<View>(R.id.card)


    }
}