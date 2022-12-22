package com.example.consultplus.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.core.util.rangeTo
import androidx.recyclerview.widget.RecyclerView
import com.example.consultplus.R
import com.example.consultplus.databinding.ItemDateBinding
import okhttp3.internal.notifyAll
import java.text.SimpleDateFormat
import java.util.*


data class DateModel(
    val date:String,
    var selected:Boolean
)


class DateAdapter(val ItemList: MutableList<DateModel>, private val onItemclicked: (date :String) -> Unit) :
    RecyclerView.Adapter<DateAdapter.ViewHolder>()   {
    private lateinit var  context: Context
    var dayOfWeek: SimpleDateFormat = SimpleDateFormat("E", Locale.getDefault())
    var dateOfWeek: SimpleDateFormat = SimpleDateFormat("dd", Locale.getDefault())
    var dateFormatNew: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateAdapter.ViewHolder {
        context = parent.context
        return ViewHolder(ItemDateBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }


    override fun getItemCount() = ItemList.size


    inner class  ViewHolder(private val binding: ItemDateBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(dateModel: DateModel) {
with(binding){
            val dateFormated = dateFormatNew.parse(dateModel.date)
            binding.day.text = dayOfWeek.format(dateFormated!!).substring(0,1)
            binding.date.text = dateOfWeek.format(dateFormated).toString()



            if (dateModel.selected) {
                root.background = ContextCompat.getDrawable(context, R.drawable.radius_box_selected)
                binding.date.setTextColor(Color.WHITE)
                binding.day.setTextColor(Color.WHITE)
            } else {
                root.background = ContextCompat.getDrawable(context, R.drawable.radius_box)
                binding.day.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryLight))
                binding.date.setTextColor(Color.BLACK)
            }
            root.setOnClickListener() {
                dateModel.selected = true
                onItemclicked(dateModel.date)
                notifyItemChanged(absoluteAdapterPosition)
            val nums = (0 until ItemList.size-1)
              nums.forEach{
                 if(it == absoluteAdapterPosition)return@forEach
                  ItemList[it].selected=false
                  notifyItemChanged(it)
              }

            }



        }
    }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ItemList[position])

    }

}