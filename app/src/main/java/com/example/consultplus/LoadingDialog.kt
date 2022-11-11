package com.malkinfo.progressbar.uitel

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.example.consultplus.R
import java.security.AccessController.getContext


class LoadingDialog(val v : View, val fragment: Fragment) {
    private lateinit var isdialog:AlertDialog
    fun startLoading(){
        /**set View*/
        val infalter = fragment.layoutInflater
        val dialogView = infalter.inflate(R.layout.loading_item,null)
        /**set Dialog*/
        val bulider = AlertDialog.Builder(v.getContext())
        bulider.setView(dialogView)
        bulider.setCancelable(false)
        isdialog = bulider.create()
        isdialog.show()
    }
    fun isDismiss(){
        isdialog.dismiss()
    }
}