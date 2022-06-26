package com.example.mobileapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobileapp.retrofit.Endpoint
import com.example.mobileapp.Parking
import kotlinx.coroutines.*

class CarViewModel : ViewModel() {
    // TODO: Implement the ViewModel

  //  var data = mutableListOf<Parking>()

    var data = MutableLiveData<List<Parking>>()
    var actualParking = MutableLiveData<Parking>()
    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable.localizedMessage)
    }


    fun loadParkings() {
        if(data.value==null) {
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = Endpoint.createEndpoint().getAllParkings()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        loading.value = false
                       data.postValue(response.body())

                    } else {
                        onError(response.message())
                    }
                }
            }

        }


    }


    private fun onError(message: String) {
        //errorMessage.value = message
      //  loading.value = false
    }






    /*
    *         Parking(1,"Parking BEO","ouvert","61%","Parking de BEO","Alger","2500km","2h","7:00h","19:00h","Dimanche",R.drawable.p1,LatLng(36.7924, 3.052),500.00),
        Parking(2,"Parking Alger","fermé","61%","Parking de BEO","Alger","2500km","2h","7:00h","19:00h","Dimanche",R.drawable.p2,LatLng(36.7924, 3.052),500.00),
        Parking(3,"Parking Ouled Fayet","ouvert","61%","Parking de BEO","Alger","2500km","2h","7:00h","19:00h","Dimanche",R.drawable.p3,LatLng(36.7924, 3.052),500.00),
        Parking(4,"Parking Rouiba","ouvert","61%","Parking de BEO","Alger","2500km","2h","7:00h","19:00h","Dimanche",R.drawable.p5,LatLng(36.7924, 3.052),500.00),
        Parking(5,"Parking Reghaia","fermé","61%","Parking de BEO","Alger","2500km","2h","7:00h","19:00h","Dimanche",R.drawable.p7,LatLng(36.7924, 3.052),500.00),
        Parking(6,"Parking El Harrach","ouvert","61%","Parking de BEO","Alger","2500km","2h","7:00h","19:00h","Dimanche",R.drawable.p1,LatLng(36.7924, 3.052),500.00),
        Parking(7,"Parking Bab ezzouar","ouvert","61%","Parking de BEO","Alger","2500km","2h","7:00h","19:00h","Dimanche",R.drawable.p2,LatLng(36.7924, 3.052),500.00),
        Parking(8,"Parking BEO","ouvert","61%","Parking de BEO","Alger","2500km","2h","7:00h","19:00h","Dimanche",R.drawable.p3,LatLng(36.7924, 3.052),500.00),
        Parking(9,"Parking BEO","fermé","61%","Parking de BEO","Alger","2500km","2h","7:00h","19:00h","Dimanche",R.drawable.p5,LatLng(36.7924, 3.052),500.00),
        Parking(10,"Parking BEO","fermé","61%","Parking de BEO","Alger","2500km","2h","7:00h","19:00h","Dimanche",R.drawable.p7,LatLng(36.7924, 3.052),500.00),
        Parking(11,"Parking BEO","ouvert","61%","Parking de BEO","Alger","2500km","2h","7:00h","19:00h","Dimanche",R.drawable.p1,LatLng(36.7924, 3.052),500.00),
        Parking(12,"Parking BEO","fermé","61%","Parking de BEO","Alger","2500km","2h","7:00h","19:00h","Dimanche",R.drawable.p2,LatLng(36.7924, 3.052),500.00)

    * */
}