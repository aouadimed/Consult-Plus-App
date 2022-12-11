
package com.example.consultplus.retrofit
import com.example.consultplus.model.User
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface Request {
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
}
