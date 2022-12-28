package com.example.consultplus.view.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.consultplus.FileUtils
import com.example.consultplus.GetImg
import com.example.consultplus.R
import com.example.consultplus.databinding.FragmentAddEtatBinding
import com.example.consultplus.databinding.FragmentMenuBinding
import com.example.consultplus.model.User
import com.example.consultplus.retrofit.Request
import com.example.consultplus.retrofit.Retrofit
import com.example.consultplus.view.ui.activity.preferences
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.util.*

class AddEtatFragment : Fragment() {
    internal var patientID: String? = null
    private  var user = User()
    lateinit var binding: FragmentAddEtatBinding
    internal var doctorID: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding =FragmentAddEtatBinding.inflate(inflater, container, false)
        val view = binding.root

        preferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        patientID = arguments?.getString(AddEtatFragment.patientId)
        email = preferences.getString("EmailUser","")
        doctorID = preferences.getString("EmailUser","")
        val retrofit: retrofit2.Retrofit = Retrofit.getInstance()
        val service: Request = retrofit.create(Request::class.java)
        user.setEmail(email)
        GetImg.Image(requireContext(),email!!,binding.img)
        // Create JSON using JSONObject

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.GetUser(user)
            withContext(Dispatchers.Main) {

                if (response.isSuccessful) {
                    user.setFullname(response.body()?.getFullname())
                    binding.fullName.text = user.getFullname()+" !"
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                    println("Message :" + response.errorBody()?.string())

                } }
        }

binding.imgDawa.visibility = View.GONE

        fun SAVE(day: String, time: String) {
            // Create JSON using JSONObject
            val jsonObject = JSONObject()
            jsonObject.put("patient",patientID)
            jsonObject.put("doctor", doctorID)
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


        binding.cardImg.setOnClickListener(){
            selectImage()
        }

        binding.imgDawa.setOnClickListener(){
            selectImage()
        }


        return view
    }
    fun UploadImage(imageUri : Uri) {
        // Create Retrofit
        val retrofit: retrofit2.Retrofit = Retrofit.getInstance()

        val service: Request = retrofit.create(Request::class.java)

        val file: File = FileUtils.from(requireContext()!!,imageUri)


        val reqFile: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())


        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("upload", file.getName(), reqFile)

        val Filename: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), "upload")


        val email = email?.let {
            RequestBody.create(
                MultipartBody.FORM, it
            )
        }

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.patientImageUpload(email!!,Filename,body)
            try {
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val prettyJson = gson.toJson(JsonParser.parseString(response.body()?.string()))
                        Log.d("Pretty Printed JSON :", prettyJson)

                    } else {

                    }
                }
            } catch (e: Exception) {
                println(e.printStackTrace())
                println("Error")
            }
        }

    }
    private  fun loadFragment(fragment: Fragment){
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment3, fragment).addToBackStack("")
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
    private fun selectImage(){

        //check runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE);
            }
            else{
                //permission already granted
                pickImageFromGallery()
            }
        }
        else{
            //system OS is < Marshmallow
            pickImageFromGallery()
        }


    }

    private fun launchImageCrop(uri: Uri) {


        var destination:String=StringBuilder(UUID.randomUUID().toString()).toString()
        var options: UCrop.Options= UCrop.Options()


        UCrop.of(Uri.parse(uri.toString()), Uri.fromFile(File(context?.cacheDir,destination)))
            .withOptions(options)
            .withAspectRatio(1F, 1F)
            .withMaxResultSize(1920, 1080)
            .start(requireContext(), this)


    }
    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }
    private fun setImage(uri: Uri){
        Glide.with(this)
            .load(uri)
            .into(binding.imgDawa)
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        pickImageFromGallery()

        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            data?.data?.let { uri ->
                launchImageCrop(uri)
            }

        }
        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            var resultUri : Uri?= UCrop.getOutput(data!!)

            setImage(resultUri!!)
            binding.cardImg.visibility = View.VISIBLE
        }
    }
    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSION_CODE = 1001
        private const val patientId = "patientID"


        // Create `SecondFragment` with bundle so you can send data from certain fragment to `SecondFragment`.
        fun newInstance(DoctorId: String) : AddEtatFragment = AddEtatFragment().apply {
            val bundle = bundleOf(
                patientId to DoctorId)
            arguments = bundle

        }}
}