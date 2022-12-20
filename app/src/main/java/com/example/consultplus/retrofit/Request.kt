
package com.example.consultplus.retrofit
import com.example.consultplus.adapter.MyObject
import com.example.consultplus.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


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


   /* @Multipart
    @POST("image")
    suspend fun uploadImage(@Body requestBody: RequestBody,@Part image: MultipartBody): Response<ResponseBody>

    @Multipart
    @POST("upload_image.php")
    suspend fun uploadFile(@Part body: MultipartBody.Part)*/
}
