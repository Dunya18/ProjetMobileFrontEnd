package com.example.mobileapp.fragments

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mobileapp.AppDatabase
import com.example.mobileapp.R
import com.example.mobileapp.adapter.ReservationAdapter
import com.example.mobileapp.viewmodel.ReservationsViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

// TODO: Rename parameter arguments, choose names that match


/**
 * A simple [Fragment] subclass.
 * Use the [DetailsReservationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsReservationFragment : Fragment() {
    lateinit var adapter : ReservationAdapter
    lateinit var reservationsViewModel : ReservationsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_details_reservation, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reservationsViewModel = ViewModelProvider(this).get(ReservationsViewModel::class.java)

        // get the data from the previous fragment

        // instanciate the database object
        val appDatabase = AppDatabase.buildDatabase(view.context)
        // get dao object
        val reservationDao = appDatabase?.getReservationDao()
        // get reservation data
        val reservation = reservationDao?.getReservationById(requireArguments().getString("reservation")!!)
        // generate code qr
        val ivQRcode = view.findViewById<ImageView>(R.id.codeQR)
        val place = view.findViewById<TextView>(R.id.numPlace)
        val reservationID = view.findViewById<TextView>(R.id.numReservation)
        // generate code qr
        val writer = QRCodeWriter()
            if (reservation != null) {
                Toast.makeText(requireContext(), "Reservation ", Toast.LENGTH_LONG).show()
                place.text = reservation.numeroPlace.toString()
                reservationID.text = reservation._id
                val numPlace = reservation.numeroPlace.toString()
                try{
                    val bitMatrix = writer.encode(numPlace, BarcodeFormat.QR_CODE,512,512)
                    val width = bitMatrix.width
                    val height = bitMatrix.height
                    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                    for(x in 0 until width){
                        for(y in 0 until height){
                            bmp.setPixel(x,y,if(bitMatrix[x,y]) Color.BLACK else Color.WHITE)
                        }
                    }
                    ivQRcode.setImageBitmap(bmp)

                }
                catch (e: WriterException){
                    e.printStackTrace()
                }
            }
            else{
                Toast.makeText(requireContext(), "NO Reservation created", Toast.LENGTH_LONG).show()
            }



    }

}