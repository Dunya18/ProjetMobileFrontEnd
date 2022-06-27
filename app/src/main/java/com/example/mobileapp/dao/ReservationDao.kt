package com.example.mobileapp.dao


import androidx.room.*
import com.example.mobileapp.entity.Reservation

@Dao
interface ReservationDao {
    @Query("select * from reservations where parking=:parkingId")
    fun getParkingReservations(parkingId: String): List<Reservation>

    @Query("select * from reservations where user = :userID")
    fun getUserReservation(userID: String): List<Reservation>

    @Query("select * from reservations where _id = :id")
    fun getReservationById(id: String): Reservation

    @Insert
    fun addReservation(vararg reservation: Reservation)

    @Insert
    fun saveReservations(reservations: List<Reservation>)

    @Delete
   fun deleteUserReservation(reservation: Reservation)
//    @Update
//    fun updateUser(user: User)
//
//    @Delete
//    fun deleteUser(user: User)
}