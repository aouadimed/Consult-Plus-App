package com.example.consultplus.view.ui.fragment






import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log

import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment

import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.view.isInvisible
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide


import com.example.consultplus.R
import com.example.consultplus.databinding.ActivityMainBinding
import com.example.consultplus.databinding.FragmentProfilUpdateBinding
import com.example.consultplus.model.User
import com.example.consultplus.retrofit.Request
import com.example.consultplus.retrofit.Retrofit
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import com.example.consultplus.view.ui.activity.preferences

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

internal var email: String? = null
internal var role: String? = null
internal var SetRole: String? = null
private  var user = User()

class ProfilUpdateFragment : Fragment() , DatePickerDialog.OnDateSetListener//, TimePickerDialog.OnTimeSetListener
{
    private val calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("MMMM d, yyyy", Locale.FRANCE)
    lateinit var binding: FragmentProfilUpdateBinding
    // This property is only valid between onCreateView and
// onDestroyView.

    private lateinit var image_select: CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =FragmentProfilUpdateBinding.inflate(inflater, container, false)
        val view = binding.root
        val adapter = ArrayAdapter(requireContext(),R.layout.spinner_item, listOf("Male", "Female"))
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.mySpinner.adapter = adapter
        val specialitList = ArrayAdapter(requireContext(),R.layout.spinner_item, listOf("Allergists","Anesthesiologists",
            "Cardiologists","Colon and Rectal Surgeons","Critical Care Medicine Specialists","Dermatologists","Endocrinologists","Family Physicians",
            "Gastroenterologists","Hematologists","Nephrologists","Neurologists","Obstetricians and Gynecologists","Ophthalmologists",
            "Otolaryngologists","Pediatricians","Physiatrists","Plastic Surgeons","Podiatrists","Radiologists"

        ))
        specialitList.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.SpinnerSpecialite.adapter = specialitList

        preferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        email = preferences.getString("EmailUser","")
        role = preferences.getString("role","")
        binding.toggleGroup.visibility = View.GONE
        binding.SpinnerSpecialite.visibility = View.GONE
        binding.description.visibility = View.GONE
        binding.numpatient.visibility = View.GONE
        binding.experience.visibility = View.GONE





        if (role.isNullOrEmpty()){
            SetRole="patient"
            binding.toggleGroup.visibility = View.VISIBLE
            binding.doctor.setOnClickListener(){
                SetRole="doctor"
                binding.SpinnerSpecialite.visibility = View.VISIBLE
                binding.description.visibility = View.VISIBLE
                binding.numpatient.visibility = View.VISIBLE
                binding.experience.visibility = View.VISIBLE
            }
            binding.patient.setOnClickListener(){
                SetRole="patient"
                binding.SpinnerSpecialite.visibility = View.GONE
                binding.description.visibility = View.GONE
                binding.numpatient.visibility = View.GONE
                binding.experience.visibility = View.GONE
            }
            binding.roudImage.visibility = View.GONE
            binding.etFullName.visibility = View.GONE
            binding.etEmail.visibility = View.GONE
        }else if(role.equals("patient")){
            binding.cardImg.visibility = View.GONE
        }else if(role.equals("doctor")){
            binding.cardImg.visibility = View.GONE
            binding.SpinnerSpecialite.visibility = View.VISIBLE
            binding.description.visibility = View.VISIBLE
            binding.numpatient.visibility = View.VISIBLE
        }









        fun updateClient(Gender: String, Bithdate: String,Adresse: String,firstname :String,lastname :String) {
            // Create Retrofit
            val retrofit: retrofit2.Retrofit = Retrofit.getInstance()

            val service: Request = retrofit.create(Request::class.java)

            // Create JSON using JSONObject
            val jsonObject = JSONObject()
            jsonObject.put("email", email)
            jsonObject.put("genders", Gender)
            jsonObject.put("birthdate", Bithdate)
            jsonObject.put("adresse", Adresse)
            jsonObject.put("firstname", firstname)
            jsonObject.put("lastname", lastname)
            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()
            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
            CoroutineScope(Dispatchers.IO).launch {
                // Do the POST request and get response
                val response = service.UpdateClinet(requestBody)
                try {
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val gson = GsonBuilder().setPrettyPrinting().create()
                            val prettyJson = gson.toJson(JsonParser.parseString(response.body()?.string()))
                            Log.d("Pretty Printed JSON :", prettyJson)
                            Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Not updated", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    println(e.printStackTrace())
                    println("Error")
                }
            }
        }
        fun updateDoctor(specialite: String, description: String,experience: Int,patient :Int) {
            // Create Retrofit
            val retrofit: retrofit2.Retrofit = Retrofit.getInstance()

            val service: Request = retrofit.create(Request::class.java)

            // Create JSON using JSONObject
            val jsonObject = JSONObject()
            jsonObject.put("email", email)
            jsonObject.put("specialite", specialite)
            jsonObject.put("description", description)
            jsonObject.put("experience", experience)
            jsonObject.put("patient", patient)
            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()
            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
            CoroutineScope(Dispatchers.IO).launch {
                // Do the POST request and get response
                val response = service.UpdateDoctor(requestBody)
                try {
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val gson = GsonBuilder().setPrettyPrinting().create()
                            val prettyJson = gson.toJson(JsonParser.parseString(response.body()?.string()))
                            Toast.makeText(context, "doctor", Toast.LENGTH_SHORT).show()
                            Log.d("Pretty Printed JSON :", prettyJson)
                        } else {
                            Toast.makeText(context, "Not updated", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    println(e.printStackTrace())
                    println("Error")
                }
            }
        }
        fun setRole() {
            // Create Retrofit
            val retrofit: retrofit2.Retrofit = Retrofit.getInstance()

            val service: Request = retrofit.create(Request::class.java)

            // Create JSON using JSONObject
            val jsonObject = JSONObject()
            jsonObject.put("email", email)
            jsonObject.put("role", SetRole)
            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()
            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
            CoroutineScope(Dispatchers.IO).launch {
                // Do the POST request and get response
                val response = service.SetRole(requestBody)
                try {
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val gson = GsonBuilder().setPrettyPrinting().create()
                            val prettyJson = gson.toJson(JsonParser.parseString(response.body()?.string()))
                            Toast.makeText(context, "role", Toast.LENGTH_SHORT).show()
                            Log.d("Pretty Printed JSON :", prettyJson)
                        } else {
                            Toast.makeText(context, "Not updated", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    println(e.printStackTrace())
                    println("Error")
                }
            }
        }
        binding.mySpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val item = adapter.getItem(position)
            }
        }

        binding.cardImg.setOnClickListener(){
            selectImage()
        }

        binding.editImage.setOnClickListener(){
            selectImage()
        }
        binding.datepicker.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

    binding.btSave.setOnClickListener(){
        preferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        if (role.isNullOrEmpty()){
            setRole()
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.putString("role", SetRole)
        if(SetRole.equals("patient")){
            updateClient(binding.mySpinner.selectedItem.toString(),binding.datepicker.text.toString(),binding.adresse.text.toString(),binding.firstname.text.toString(),binding.lastname.text.toString())
            loadFragment(HomeFragment())
        }else if(SetRole.equals("doctor")){
            updateClient(binding.mySpinner.selectedItem.toString(),binding.datepicker.text.toString(),binding.adresse.text.toString(),binding.firstname.text.toString(),binding.lastname.text.toString())
            updateDoctor(binding.SpinnerSpecialite.selectedItem.toString(),binding.description.text.toString(),Integer.parseInt(binding.experience.text.toString()),Integer.parseInt(binding.numpatient.text.toString()))
            loadFragment(MenuFragment())
        }
        }

        if(role.equals("patient")){

            updateClient(binding.mySpinner.selectedItem.toString(),binding.datepicker.text.toString(),binding.adresse.text.toString(),binding.firstname.text.toString(),binding.lastname.text.toString())
            loadFragment(ProfilUpdateFragment())
        }else if(role.equals("doctor")){

            updateClient(binding.mySpinner.selectedItem.toString(),binding.datepicker.text.toString(),binding.adresse.text.toString(),binding.firstname.text.toString(),binding.lastname.text.toString())
            updateDoctor(binding.SpinnerSpecialite.selectedItem.toString(),binding.description.text.toString(),Integer.parseInt(binding.experience.text.toString()),Integer.parseInt(binding.numpatient.text.toString()))
            loadFragment(ProfilUpdateFragment())
        }


    }

        return view
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        displayFormattedDate(calendar.timeInMillis)
    }

  /*  override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }
        displayFormattedDate(calendar.timeInMillis)
    }*/

    private fun displayFormattedDate(timestamp: Long) {
        binding.datepicker.setText(formatter.format(timestamp))
        Log.i("Formatting", timestamp.toString())
    }
    private  fun loadFragment(fragment: Fragment){
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment, fragment).addToBackStack("").commit()
    }
    private fun selectImage(){

        //check runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE) ==
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
        var options: UCrop.Options=UCrop.Options()


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
            .into(binding.imgProfil)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSION_CODE = 1001

    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            data?.data?.let { uri ->
                launchImageCrop(uri)
            }

        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri :Uri ?= UCrop.getOutput(data!!)
            setImage(resultUri!!)
            binding.cardImg.visibility = View.GONE
            binding.roudImage.visibility = View.VISIBLE
        }
    }
    }
