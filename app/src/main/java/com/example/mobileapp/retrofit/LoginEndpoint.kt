package com.example.mobileapp.retrofit

import com.example.mobileapp.entity.User
import com.example.mobileapp.url
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginEndpoint {
    @POST("auth/login")
    @FormUrlEncoded
    suspend fun login(@Field("email") email : String,
                        @Field("password") password: String): Response<User>
    @POST("auth/register")
    @FormUrlEncoded
    suspend fun signup(@Field("name") name : String,
                       @Field("family_name") family_name : String,
                       @Field("email") email : String,
                       @Field("phone_number") phone_number : String,
                      @Field("password") password: String): Response<User>
    companion object {
        var endpoint: LoginEndpoint? = null
        fun createEndpoint(): LoginEndpoint {
            if (endpoint == null) {
                endpoint = Retrofit.Builder().baseUrl(url).addConverterFactory(
                    GsonConverterFactory.create()
                ).build().create(
                    LoginEndpoint::class.java
                )
            }

            return endpoint!!
        }
    }
}