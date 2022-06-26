package com.example.mobileapp.retrofit
import com.example.mobileapp.Parking
import com.example.mobileapp.url
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface SearchEndPoint {
    @FormUrlEncoded
    @POST("parkings/search")
    suspend fun searchByNom(@FieldMap data: Map<String, String?>): Response<List<Parking>>

    companion object {
        private var endpoint: SearchEndPoint? = null
        fun createEndpoint(): SearchEndPoint {
            if (endpoint == null) {
                endpoint = Retrofit.Builder().baseUrl(url).addConverterFactory(
                    GsonConverterFactory.create()
                ).build().create(
                    SearchEndPoint::class.java
                )
            }
            return endpoint!!
        }
    }
}