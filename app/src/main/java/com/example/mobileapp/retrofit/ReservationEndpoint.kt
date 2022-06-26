package com.example.mobileapp.retrofit



import com.example.mobileapp.Parking
import com.example.mobileapp.entity.Reservation
import com.example.mobileapp.entity.User
import com.example.mobileapp.url
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*

interface ReservationEndpoint {

    @POST("reservations/create")
    @FormUrlEncoded
    suspend fun addRservation(@Field("dateEntree") dateEntree : Date,
                       @Field("dateSortie") dateSortie : Date,
                       @Field("parkingId") parkingId : String,
                       @Field("userId") userId : String): Response<Reservation>

    @GET("reservations/{id}")
    suspend fun getUserReservation(@Path("id") id: String?): Response<List<Reservation>>


    companion object {
        @Volatile
        var endpoint: ReservationEndpoint? = null

        fun createEndpoint(): ReservationEndpoint {
            if(endpoint ==null) {
                val gson =  GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm")
                    .create()
                    endpoint = Retrofit.Builder().baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create(gson)).build()
                        .create(ReservationEndpoint::class.java)

            }
            return endpoint!!

        }


    }

}