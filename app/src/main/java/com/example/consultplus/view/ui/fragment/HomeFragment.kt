package com.example.consultplus.view.ui.fragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.consultplus.R
import com.example.consultplus.adapter.MostPopularAdapter
import com.example.consultplus.adapter.MyObject
import com.example.consultplus.adapter.TestAdapter
import com.example.consultplus.adapter.TestObject
import com.example.consultplus.model.User
import com.example.consultplus.retrofit.Request
import com.example.consultplus.retrofit.Retrofit
import com.example.consultplus.view.ui.activity.preferences
import kotlinx.coroutines.*


class HomeFragment : Fragment() {

    lateinit var recylcerMostPopularAdapter: MostPopularAdapter
    lateinit var recylcerMostPopular: RecyclerView


    lateinit var recylceTestAdapter: TestAdapter
    lateinit var recylcerTest: RecyclerView


    private lateinit var nameTag: TextView
    private lateinit var test_tile: TextView
    private lateinit var test_tile_2: TextView
    lateinit var sharedPreferences: SharedPreferences
    internal var email: String? = null
    private  var user = User()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_home, container, false)

        nameTag = view.findViewById(R.id.full_name)

        val retrofit: retrofit2.Retrofit = Retrofit.getInstance()
        val service: Request = retrofit.create(Request::class.java)

        preferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        email = preferences.getString("EmailUser","")

        user.setEmail(email)
        // Create JSON using JSONObject

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.GetUser(user)
            withContext(Dispatchers.Main) {

            if (response.isSuccessful) {
                user.setFullname(response.body()?.getFullname())
                nameTag.text = user.getFullname()+" !"
            } else {
                Log.e("RETROFIT_ERROR", response.code().toString())
                println("Message :" + response.errorBody()?.string())

            } }
        }




        ////most popular

        recylcerMostPopular = view.findViewById(R.id.boxRecyclerView)


        var boxList : MutableList<MyObject> = ArrayList()

        boxList.add(MyObject(EmojiPic = R.drawable.dentistry_emoji, specialties = "Dentistry", specialties_count = "70" ))
        boxList.add(MyObject(EmojiPic = R.drawable.cardiology_emoji, specialties = "Cardiology", specialties_count = "51" ))
        boxList.add(MyObject(EmojiPic = R.drawable.dentistry_emoji, specialties = "Dentistry", specialties_count = "70" ))
        boxList.add(MyObject(EmojiPic = R.drawable.cardiology_emoji, specialties = "Cardiology", specialties_count = "51" ))
        boxList.add(MyObject(EmojiPic = R.drawable.dentistry_emoji, specialties = "Dentistry", specialties_count = "70" ))
        boxList.add(MyObject(EmojiPic = R.drawable.cardiology_emoji, specialties = "Cardiology", specialties_count = "51" ))
        boxList.add(MyObject(EmojiPic = R.drawable.dentistry_emoji, specialties = "Dentistry", specialties_count = "70" ))
        boxList.add(MyObject(EmojiPic = R.drawable.cardiology_emoji, specialties = "Cardiology", specialties_count = "51" ))
        boxList.add(MyObject(EmojiPic = R.drawable.dentistry_emoji, specialties = "Dentistry", specialties_count = "70" ))
        boxList.add(MyObject(EmojiPic = R.drawable.cardiology_emoji, specialties = "Cardiology", specialties_count = "51" ))


        recylcerMostPopularAdapter = MostPopularAdapter(boxList)

        recylcerMostPopular.adapter =  recylcerMostPopularAdapter

        recylcerMostPopular.layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL ,false)


        ////////testtttt

        recylcerTest = view.findViewById(R.id.testRecyclerView)
        test_tile = view.findViewById(R.id.test_title)



        var TestList : MutableList<TestObject> = ArrayList()


        TestList.add(TestObject(test_name = "General blood analysis", test_status = "ready" ))
        TestList.add(TestObject(test_name = "Antibody test COVID-19", test_status = "in the process" ))




        recylceTestAdapter = TestAdapter(TestList)

        recylcerTest.adapter =  recylceTestAdapter

        recylcerTest.layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL ,false)


       // test_tile.visibility = View.INVISIBLE



        return view
    }

 }
