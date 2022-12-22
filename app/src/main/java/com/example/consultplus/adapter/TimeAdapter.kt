package com.example.consultplus.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.consultplus.R
import com.example.consultplus.databinding.ItemTimeBinding

data class TimeModel(
    var time:Int,
    var selected:Boolean,
    var check:Boolean
)
data class TimeCheck(
    val time:String,
)
class TimeAdapter(val ItemList :MutableList<TimeModel>, private val onItemclicked: (time :String) -> Unit) :
        RecyclerView.Adapter<TimeAdapter.ViewHolder>(){
    private lateinit var  context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeAdapter.ViewHolder {
        context = parent.context
        return ViewHolder(ItemTimeBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount() = ItemList.size


    inner class  ViewHolder(private val binding: ItemTimeBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(timeModel: TimeModel) {
            with(binding){
                var time:String = ""
                var endTime:String = ""
              if(timeModel.time<10){
                  time ="0"+timeModel.time.toString()+":00"
              }else{
                  time = timeModel.time.toString()+":00"
              }
            var endtime:Int = timeModel.time+1

                if(endtime<10){
                    endTime = "0$endtime:00"
                }else{
                    endTime = "$endtime:00"
                }

               binding.time.text = time
                binding.end.text = endTime

                if(timeModel.check){
                    root.background = ContextCompat.getDrawable(context, R.drawable.radius_box_taken)
                    binding.time.setTextColor(Color.WHITE)
                    binding.end.setTextColor(Color.WHITE)
                }else if (timeModel.selected) {
                    root.background = ContextCompat.getDrawable(context, R.drawable.radius_box_selected)
                    binding.time.setTextColor(Color.WHITE)
                    binding.end.setTextColor(Color.WHITE)
                } else {
                    root.background = ContextCompat.getDrawable(context, R.drawable.radius_box)
                    binding.end.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryLight))
                    binding.time.setTextColor(Color.BLACK)
                }
                root.setOnClickListener() {
                    if(timeModel.check){
                        Toast.makeText(context, "This date is taken", Toast.LENGTH_SHORT).show()
                    }else{
                        timeModel.selected = true
                        onItemclicked(time)
                        notifyItemChanged(absoluteAdapterPosition)
                        val nums = (0 until ItemList.size)
                        nums.forEach{
                            if(it == absoluteAdapterPosition)return@forEach
                            ItemList[it].selected=false
                            notifyItemChanged(it)
                        }
                    }

                }



            }
        }
    }




    override fun onBindViewHolder(holder: TimeAdapter.ViewHolder, position: Int) {
        holder.bind(ItemList[position])

    }
}