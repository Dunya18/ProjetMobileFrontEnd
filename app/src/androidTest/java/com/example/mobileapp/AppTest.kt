package com.example.mobileapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.mobileapp.entity.Reservation
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class AppTest {
    lateinit var mDataBase: AppDatabase

    @Before
    fun initDB() {
        mDataBase =
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AppDatabase::class.java
            ).build()
    }

    @Test
    fun testInsertAndGetUser() {
        val reservation = Reservation(
            1,
            "test@gmail.com",
            1, LocalDateTime.now().toString()
        )
        mDataBase.getReservationDao().addReservation(reservation)
        val res = mDataBase.getReservationDao().getReservationById((1).toString())
        assertEquals(reservation.reservationId, res.reservationId)
    }

    @Test
    fun testGetAllReservations() {
        val reservations = arrayOf(
            Reservation(
                1,
                "test@gmail.com",
                1, LocalDateTime.now().toString()
            ),
            Reservation(
                2,
                "test@gmail.com",
                1, LocalDateTime.now().toString()
            ),
            Reservation(
                3,
                "test@gmail.com",
                1, LocalDateTime.now().toString()
            )
        )

        for (reservation in reservations) {
            mDataBase.getReservationDao().addReservation(reservation)
        }
        val res = mDataBase.getReservationDao().getAllReservations("test@gmail.com")
        var index = 0
        for (result in res) {
            assertEquals(result.reservationId, reservations[index].reservationId)
            index++
        }
    }

    @Test
    fun testGetReservationByDate() {
        val reservations = arrayOf(
            Reservation(
                1,
                "test@gmail.com",
                1, "05-May-2022"
            ),
            Reservation(
                2,
                "test@gmail.com",
                1, LocalDateTime.now().toString()
            ),
            Reservation(
                3,
                "test@gmail.com",
                1, LocalDateTime.now().toString()
            )
        )

        for (reservation in reservations) {
            mDataBase.getReservationDao().addReservation(reservation)
        }
        val res = mDataBase.getReservationDao().getReservationByDate("05-May-2022")
        assertEquals(res.size, 1)
    }

    @After
    fun closeDb() {
        mDataBase.close()
    }
}