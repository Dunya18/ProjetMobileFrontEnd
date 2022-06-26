package com.example.mobileapp.entity

data class User(
    val id: String,
    val name: String?,
    val family_name: String?,
    val email: String?,
    val phone_number: String?,
    val password: String?,
    val photoLink: String?,
    val token : String?
)
