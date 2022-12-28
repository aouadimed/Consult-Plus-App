package com.example.consultplus

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

object GetImg {

     fun Image(context: Context,email:String,Image:ImageView){
        Glide.get(context).clearMemory();
        Glide.with(context)
            .load("http://192.168.1.11:5000/getImage/"+email)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(Image)
    }
    fun Imageadapter(context: Context,email:String,Image:ImageView){
        Glide.with(context)
            .load("http://192.168.1.11:5000/getImage/"+email)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(Image)
    }

}