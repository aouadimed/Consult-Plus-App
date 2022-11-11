package com.example.consultplus.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.example.consultplus.R

data class MyObject(@DrawableRes val EmojiPic: Int, val specialties : String, val specialties_count: String)

class MostPopularAdapter(val ItemList: MutableList<MyObject>) : RecyclerView.Adapter<MostPopularAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.most_popular_layout, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val count = ItemList[position].specialties_count
        val specialties = ItemList[position].specialties

        holder.emoji.setImageResource(ItemList[position].EmojiPic)
        holder.count.text = count
        holder.specialties.text = specialties



    }

    override fun getItemCount() = ItemList.size

    class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val count =itemView.findViewById<TextView>(R.id.specialties_count)
        val specialties =itemView.findViewById<TextView>(R.id.specialties)
        val emoji =itemView.findViewById<ImageView>(R.id.emoji)


    }
}