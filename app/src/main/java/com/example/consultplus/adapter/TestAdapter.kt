package com.example.consultplus.adapter

import android.content.res.Resources
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.graphics.blue
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.RecyclerView
import com.example.consultplus.R

data class TestObject(val test_name : String, val test_status: String)

class TestAdapter(val ItemList: MutableList<TestObject>) : RecyclerView.Adapter<TestAdapter.TestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.test_layout, parent, false)

        return TestViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {


        val status = ItemList[position].test_status
        val test_name = ItemList[position].test_name

        if(status.equals("ready")){
            holder.status.setTextColor(Color.GREEN)
            holder.circle.background.colorFilter = BlendModeColorFilter(Color.rgb(225,117,117), BlendMode.SRC_ATOP)

        }
        if(status.equals("in the process")){
            holder.status.setTextColor(Color.GRAY)

            holder.circle.background.colorFilter = BlendModeColorFilter(Color.GRAY, BlendMode.SRC_ATOP)

        }
        holder.status.text = status
        holder.test_name.text = test_name



    }

    override fun getItemCount() = ItemList.size

    class TestViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val status =itemView.findViewById<TextView>(R.id.test_status)
        val test_name =itemView.findViewById<TextView>(R.id.test_name)
        val circle =itemView.findViewById<View>(R.id.circle)

    }

}