package com.example.mobileapp

import java.io.Serializable

data class Parking (

    var _id: String,
    var imglink:String?,
    var nom : String?,
    var commune: String?,
    var latitude: Double?,
    var longitude: Double?,
    var horraireOuver : Int?,
    var horraireFerm : Int?,
    var tarifHeure : String?,
    var nbPlace: Int?,
    var reserved: Int?
    ):Serializable
{
}

data class LatLng(
    var lat: Double,
    var lng: Double,
) : Serializable