
package com.example.consultplus.retrofit
import com.example.consultplus.model.User
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface Request {
    @POST("signin")
    suspend fun Login(@Body User: User): Response<User>

    @POST("signup")
    suspend fun Signup(@Body requestBody: RequestBody): Response<ResponseBody>

}
