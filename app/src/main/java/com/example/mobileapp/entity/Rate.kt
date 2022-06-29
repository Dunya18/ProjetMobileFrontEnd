package com.example.mobileapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Rate")
data class Rate(

             var comment: String?,
             var note: Double,
             var user: String,
            var parking: String,
             @PrimaryKey
             var _id: String
)