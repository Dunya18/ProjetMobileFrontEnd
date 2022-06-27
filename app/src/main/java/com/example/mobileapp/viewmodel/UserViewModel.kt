package com.example.mobileapp.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobileapp.MainActivity
import com.example.mobileapp.entity.User
import com.example.mobileapp.retrofit.LoginEndpoint
import kotlinx.coroutines.*

class UserViewModel : ViewModel() {
    var loading = MutableLiveData<Boolean>()
    var message = MutableLiveData<String>()
    var isAuth = MutableLiveData<Boolean>()
    var user = MutableLiveData<User>()

    fun login(email: String, password: String) {
        System.out.print("morning")
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            // traitement de l’exception
            message.value = "Une erreur s'est produit"
        }

        loading.value = true
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = LoginEndpoint.createEndpoint().login(email, password)
            withContext(Dispatchers.Main) {
                try {
                    loading.value = false
                    if (response.isSuccessful) {
                        // or response.code() == 200
                        val data = response.body()
                        // user.value = data!!
                        if (data != null) {
                            // Save the user

                            isAuth.value = true
                            // add user
                            user.value = data!!
                            print("afternon")
                        } else {

                            isAuth.value = false
                            message.value = data.toString()
                        }
                    } else {

                        message.value = "Une erreur s'est produit"
                    }
                }catch (e:Exception){
                    Log.d("you are offline","offline")
                }
            }
        }
    }
    fun signup(name: String, family_name: String, phone_number: String, email: String, password: String) {

        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            // traitement de l’exception
            message.value = "Une erreur s'est produit"
        }
        loading.value = true
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = LoginEndpoint.createEndpoint().signup(name, family_name, email, phone_number, password)
            withContext(Dispatchers.Main) {
                loading.value = false
                if (response.isSuccessful) {
                    // or response.code() == 200
                    val data = response.body()
                    if (data != null) {
                        // Save the user
                        isAuth.value = true
                        // add user
                        user.postValue(User(data.id,data.name,data.family_name,data.email,data.phone_number,data.password,data.photoLink,data.token))
                    } else {
                        isAuth.value = false
                        message.value = data.toString()
                    }
                } else {
                    message.value = "Une erreur s'est produit"
                }
            }
        }
    }



}