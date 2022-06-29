package com.example.mobileapp.retrofit

import com.example.mobileapp.Parking
import com.example.mobileapp.entity.Rate
import com.example.mobileapp.url
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RateEndpoint {

    @GET("user/rate/{userId}")
    suspend fun getRatedParking(@Path("userId") userId:String): Response<List<Rate>>

    @POST("user/rate")
    @FormUrlEncoded
    suspend fun rateParking(@Field("parkingId") parkingId: String,
                            @Field("note") note: Double,
                            @Field("comment") comment: String?,
                            @Field("userId") userId:String): Response<Rate>

    companion object {
        @Volatile
        var endpoint: RateEndpoint? = null

        fun createEndpoint(): RateEndpoint {
            if(endpoint ==null) {
                synchronized(this) {
                    endpoint = Retrofit.Builder().baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create()).build()
                        .create(RateEndpoint::class.java)
                }
            }
            return endpoint!!

        }


    }

}