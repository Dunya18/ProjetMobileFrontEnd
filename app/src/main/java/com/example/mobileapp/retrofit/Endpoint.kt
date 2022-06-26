package com.example.mobileapp.retrofit


import com.example.mobileapp.Parking
import com.example.mobileapp.url
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface Endpoint {

    @GET("parkings")
    suspend fun getAllParkings(): Response<List<Parking>>

    @GET("parkings/{id}")
    suspend fun getParkingByID(@Path("id") id: String?): Response<Parking>

    companion object {
        @Volatile
        var endpoint: Endpoint? = null

        fun createEndpoint(): Endpoint {
            if(endpoint ==null) {
                synchronized(this) {
                    endpoint = Retrofit.Builder().baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create()).build()
                        .create(Endpoint::class.java)
                }
            }
            return endpoint!!

        }


    }

}