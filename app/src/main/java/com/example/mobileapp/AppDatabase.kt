package com.example.mobileapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mobileapp.dao.ReservationDao
import com.example.mobileapp.entity.Reservation

@Database(entities = [Reservation::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getReservationDao(): ReservationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun buildDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "db_reservation")
                        .allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }
    }
}