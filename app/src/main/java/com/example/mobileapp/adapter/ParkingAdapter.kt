package com.example.mobileapp.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mobileapp.Parking
import com.example.mobileapp.R
import java.util.*


class ParkingAdapter (val context: Context): RecyclerView.Adapter<ParkingAdapter.MyViewHolder>()
{
    var data = mutableListOf<Parking>()

    fun setParkings(parkings: List<Parking>) {
        this.data = parkings.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.car_layout, parent, false))

    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.apply {
            //occup.text = data[position].taux_occupation
           // etat.text =  data[position].etat_actuel
            val rightNow = Calendar.getInstance()
            val currentHourIn24Format: Int =rightNow.get(Calendar.HOUR_OF_DAY) // return the hour in 24 hrs format (ranging from 0-23)
            if (data[position].horraireFerm!! > currentHourIn24Format)
            {
                etat.setTextColor(Color.parseColor("#0aad3f"))
                etat.setText("Ouvert")
            }
            else{
                etat.setTextColor(Color.parseColor("#FF0000"))
                etat.setText("Fermé")
            }
          //  dist.text = data[position].dist
          //  temps.text =  data[position].temps

            taux.text = data[position].nbPlace.toString() + " places"
            nom.text =  data[position].nom
            lieu.text = data[position].commune
            wilaya.text =  data[position].commune


            // set image

            Glide.with(this.itemView).load(data[position].imglink)
                .apply(RequestOptions())
                .placeholder(R.drawable.p1)
                .into(img)

           // DownloadImageFromInternet(img)

          //  img.setImageResource(R.drawable.p1)

            itemView.setOnClickListener {
                val bundle = bundleOf("parking" to data[position]._id)
                holder.itemView.findNavController()
                    .navigate(R.id.action_carFragment_to_detailsFragment, bundle)
            }

        }



    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val nom = view.findViewById (R.id.nom) as TextView
        val etat = view.findViewById (R.id.etat) as TextView
        val lieu = view.findViewById (R.id.commune) as TextView
        val wilaya = view.findViewById (R.id.commune) as TextView
        val taux = view.findViewById (R.id.taux) as TextView
        val img = view.findViewById (R.id.img) as ImageView


    }



}
