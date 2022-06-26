package com.example.mobileapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobileapp.Parking
import com.example.mobileapp.retrofit.SearchEndPoint
import kotlinx.coroutines.*

class SearchViewModel: ViewModel() {
    var loading = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()
    var searchList = MutableLiveData<List<Parking>>()



    fun searchByNom(term: String) {
        val body = LinkedHashMap<String, String?>()
        body["term"] = term

        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            // traitement de l’exception
            message.value = "Une erreur s'est produit"
        }
        loading.value = true
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = SearchEndPoint.createEndpoint().searchByNom(body)
            withContext(Dispatchers.Main) {
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
                }
            }
        }
    }
}