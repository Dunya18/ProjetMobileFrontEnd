package com.example.mobileapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "reservations")
data class Reservation(

    @PrimaryKey(autoGenerate = true)
    var _id: String,
    var dateEntree: Date,
    var dateSortie: Date,
    var numeroPlace:Int,
    var parking: String,
    var user: String

)