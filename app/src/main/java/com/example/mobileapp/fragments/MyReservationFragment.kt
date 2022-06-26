package com.example.mobileapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.mobileapp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [MyReservationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyReservationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    var test = true
    var isauthenticated = true
    lateinit var sharedPreferences : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("app_state", Context.MODE_PRIVATE)
        isauthenticated = sharedPreferences.getBoolean("is_authenticated",false)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_my_reservation, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isauthenticated) {
            view.findNavController()
                .navigate(R.id.action_myReservationFragment_to_loginFragment)
        }else{
              view.findNavController()
                .navigate(R.id.action_myReservationFragment_to_reservationFragment)

        }

    }

}