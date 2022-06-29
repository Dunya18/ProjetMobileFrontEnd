package com.example.mobileapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.example.mobileapp.R
import com.example.mobileapp.adapter.ReservationAdapter
import com.example.mobileapp.viewmodel.CarViewModel
import com.example.mobileapp.viewmodel.RateViewModel
import com.example.mobileapp.viewmodel.ReservationsViewModel
import org.w3c.dom.Text


/**
 * A simple [Fragment] subclass.
 * Use the [RateParkingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RateParkingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var sharedPreferences : SharedPreferences
    lateinit var parkingViewModel : CarViewModel
    lateinit var rateViewModel : RateViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_rate_parking, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rateViewModel = ViewModelProvider(requireActivity()).get(RateViewModel::class.java)

        // get user and parking informations
        sharedPreferences = requireActivity().getSharedPreferences("app_state", Context.MODE_PRIVATE)
        parkingViewModel = ViewModelProvider(requireActivity()).get(CarViewModel::class.java)

        var id = sharedPreferences.getString("id","DEFAULT")
        var parkingId = parkingViewModel.actualParking.value!!._id

        // get rates and comments
        var rBar = view.findViewById<RatingBar>(R.id.rBar)
        var comment = view.findViewById<TextView>(R.id.commentaire) as EditText
        var confirme = view.findViewById<Button>(R.id.ajouter)
        if(id != null) {
            confirme.setOnClickListener {
                var msg = rBar.rating.toDouble()
                rateViewModel.rateParking(parkingId, msg, comment.text.toString(), id)
                rateViewModel.createdData.observe(this) { data ->
                   if (data != null)

                       Toast.makeText(
                           requireContext(),
                           "Your rating : " + msg+ " is succefully added", Toast.LENGTH_SHORT
                       ).show()
              }
            }
        }

        }
    }
