package com.example.mobileapp.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.Parking
import com.example.mobileapp.R
import com.example.mobileapp.entity.Reservation
import com.example.mobileapp.viewmodel.UserViewModel


class ReservationAdapter(val context: Context) :
    RecyclerView.Adapter<ReservationAdapter.ResViewHolder>() {
    var data = mutableListOf<Reservation>()

    fun setReservations(reservation: List<Reservation>) {
        this.data = reservation.toMutableList()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResViewHolder {
        return ResViewHolder(
            LayoutInflater.from(context).inflate(R.layout.reservation_layout, parent, false)
        )

    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ResViewHolder, position: Int) {
        holder.apply {
            numPlace.text = data[position].numeroPlace.toString()
            HeureEntrée.text = data[position].dateEntree.toString()
            HeureSorite.text = data[position].dateSortie.toString()
        }


    }

    class ResViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        val numPlace = view.findViewById(R.id.numPlace) as TextView
        val HeureEntrée = view.findViewById(R.id.HeureEntree) as TextView
        val HeureSorite = view.findViewById(R.id.HeureSortie) as TextView


    }

}
