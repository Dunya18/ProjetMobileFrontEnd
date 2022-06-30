package com.example.mobileapp.adapter



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.Parking
import com.example.mobileapp.R
import com.example.mobileapp.entity.Rate
import com.example.mobileapp.entity.Reservation
import com.example.mobileapp.viewmodel.CarViewModel
import com.example.mobileapp.viewmodel.UserViewModel


class RateAdapter(val context: Context) :
    RecyclerView.Adapter<RateAdapter.ResViewHolder>() {
    var data = mutableListOf<Rate>()


    fun setRates(rate: List<Rate>) {
        this.data = rate.toMutableList()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResViewHolder {
        return ResViewHolder(
            LayoutInflater.from(context).inflate(R.layout.myrates_layout, parent, false)
        )

    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ResViewHolder, position: Int) {
        holder.apply {
            noteParking.rating = data[position].note.toFloat()
            comment.text = data[position].comment.toString()
        }



    }

    class ResViewHolder(view: View) : RecyclerView.ViewHolder(view) {



        var noteParking = view.findViewById<RatingBar>(R.id.noteParking)
        var comment = view.findViewById<TextView>(R.id.commentParking) as TextView


    }

}
