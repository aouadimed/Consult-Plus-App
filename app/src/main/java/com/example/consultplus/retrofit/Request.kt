
package com.example.consultplus.retrofit
import com.example.consultplus.adapter.Doctor
import com.example.consultplus.adapter.MyObject
import com.example.consultplus.model.Booking
import com.example.consultplus.model.PatientBooking
import com.example.consultplus.model.User
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

data class ProfileImageUploadModel(
    val code: Int,
    val status: Int,
    val message: String,
    val profile_picture: String
)
data class CheckTime(@SerializedName("time") @Expose val time: String)

interface Request {
    // A suspending function is simply a function that can be paused and resumed at a later time. They can execute a long running operation and wait for it to complete without blocking.
    @POST("login")
    suspend fun Login(@Body User: User): Response<User>

    @POST("register")
    suspend fun Signup(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("userdata")
    suspend fun GetUser(@Body User: User): Response<User>

    @POST("edituser")
    suspend fun UpdateClinet(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("editmedecin")
    suspend fun UpdateDoctor(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("editrole")
    suspend fun SetRole(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("forget-password")
    suspend fun getemail(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("reset-password")
    suspend fun resetPassword(@Body requestBody: RequestBody): Response<ResponseBody>

    @GET("groupspecialiter")
    fun mostPopuler(): Call<List<MyObject>>

    @GET("recherche/specialite")
    fun getbyspecialite(@Header("specialite") specialite: String): Call<List<Doctor>>

    @GET("doctordata")
    fun getAllDoctor(): Call<List<Doctor>>

    @POST("recherche/doctor")
    suspend fun GetDoctor(@Body User: User): Response<User>

    @POST("addbooking")
    suspend fun Book(@Body requestBody: RequestBody): Response<ResponseBody>

    @GET("/recherche/time")
    fun Gettimes(@Header("doctor") doctorID: String,@Header("date") Date: String): Call<List<CheckTime>>

    @GET("recherche/booking")
    fun getAppointmentsBydoctor(@Header("doctor") doctorID: String): Call<List<Booking>>

    @GET("recherche/bookingforpatient")
    fun getAppointmentsBypatient(@Header("patient") doctorID: String): Call<List<PatientBooking>>

    @POST("editstatu")
    suspend fun EditStatus(@Body requestBody: RequestBody): Response<ResponseBody>


    @HTTP(method = "DELETE", path = "deletebooking", hasBody = true)
    suspend fun DeleteBokking(@Body requestBody: RequestBody): Response<ResponseBody>



    @Multipart
    @POST("ImageUpload")
    suspend fun patientImageUpload(
        @Part("email") email: RequestBody,
        @Part("upload") name: RequestBody,
        @Part file: MultipartBody.Part?
    ): Response<ResponseBody>
}
