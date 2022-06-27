package com.example.mobileapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobileapp.Parking
import com.example.mobileapp.retrofit.ReservationEndpoint
import com.example.mobileapp.retrofit.SearchEndPoint
import kotlinx.coroutines.*

class SearchViewModel: ViewModel() {
    var loading = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()
    var searchList = MutableLiveData<List<Parking>>()
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable.localizedMessage)
    }



    fun searchByNom(term: String) {
        val body = LinkedHashMap<String, String?>()
        body["term"] = term

        loading.value = true
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = SearchEndPoint.createEndpoint().searchByNom(body)
            withContext(Dispatchers.Main) {
                try{
                loading.value = false
                if (response.isSuccessful) {
                    // or response.code() == 200
                    val data = response.body()
                    Log.d("data", data.toString())
                    if (data != null) {
                        searchList.value = data!!
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


    fun searchNearestParking(address: String) {
        loading.value = true
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response =
                SearchEndPoint.createEndpoint().searchNearestParking(address)
            withContext(Dispatchers.Main) {
                try {
                    loading.value = false
                    if (response.isSuccessful) {
                        // or response.code() == 200
                        val data = response.body()
                        if (data != null) {
                            searchList.value = data!!
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

    private fun onError(message: String) {
        //errorMessage.value = message
        //  loading.value = false
    }



}