package com.example.mobileapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobileapp.entity.Reservation
import com.example.mobileapp.retrofit.ReservationEndpoint
import kotlinx.coroutines.*
import java.util.*

class ReservationsViewModel : ViewModel() {
    var reservations = MutableLiveData<List<Reservation>>()
    var reservation = MutableLiveData<Reservation>()
    var loading = MutableLiveData<Boolean>()
    var created = MutableLiveData<Boolean>()
    var message = MutableLiveData<String>()
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable.localizedMessage)
    }
    fun getUserReservations(reservationId: String) {
        loading.value = true
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

                val response =
                    ReservationEndpoint.createEndpoint().getUserReservation(reservationId)
                withContext(Dispatchers.Main) {
                    try {
                    loading.value = false
                    if (response.isSuccessful) {
                        // or response.code() == 200
                        val data = response.body()
                        if (data != null) {
                            reservations.value = data!!
                        } else {
                            message.value = data.toString()
                        }
                    } else {
                        message.value = "Une erreur s'est produit"
                        onError(response.message())
                    }
                }catch (e:Exception){
                        Log.d("you are offline","offline")
                    }
            }
        }
    }
    fun addReservation(dateEntree : Date,dateSortie : Date,parkingId : String,userId : String) {

        loading.value = true
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = ReservationEndpoint.createEndpoint().addRservation(dateEntree,dateSortie,parkingId,userId)
            withContext(Dispatchers.Main) {
                loading.value = false
                if (response.isSuccessful) {
                    // or response.code() == 200
                    val data = response.body()
                    if (data != null) {
                        created.value = true
                        reservation.value = data!!
                    } else {
                        created.value = false
                        message.value = data.toString()
                    }
                } else {
                    message.value = "No places available"
                    onError(response.message())
                }
            }
        }
    }
    private fun onError(message: String) {
        //errorMessage.value = message
        //  loading.value = false
    }
}