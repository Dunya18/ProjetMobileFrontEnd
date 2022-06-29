package com.example.mobileapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobileapp.entity.Rate
import com.example.mobileapp.retrofit.RateEndpoint
import kotlinx.coroutines.*
import retrofit2.http.Field

class RateViewModel : ViewModel() {
    // TODO: Implement the ViewModel


    var data = MutableLiveData<List<Rate>>()
    var loading = MutableLiveData<Boolean>()
    val createdData = MutableLiveData<Rate>()
    val errorMessage = MutableLiveData<String>()
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable.localizedMessage)
    }


    fun getRatedParking(parkingId : String) {
        if(data.value==null) {
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

                val response = RateEndpoint.createEndpoint().getRatedParking(parkingId)
                withContext(Dispatchers.Main) {
                    try {
                        if (response.isSuccessful && response.body() != null) {
                            loading.value = false
                            data.postValue(response.body())

                        } else {
                            onError(response.message())
                        }
                    }catch (e:Exception){
                        Log.d("you are offline","offline")
                    }
                }
            }

        }


    }
    fun rateParking(parkingId: String,note: Double, comment: String,userId:String) {
        if(data.value==null) {
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

                val response = RateEndpoint.createEndpoint().rateParking(parkingId, note, comment, userId)
                withContext(Dispatchers.Main) {
                    try {
                        if (response.isSuccessful && response.body() != null) {
                            loading.value = false
                            createdData.postValue(response.body())

                        } else {
                            onError(response.message())
                        }
                    }catch (e:Exception){
                        Log.d("you are offline","offline")
                    }
                }
            }

        }


    }

    private fun onError(message: String) {
        //errorMessage.value = message
        //  loading.value = false
    }

}