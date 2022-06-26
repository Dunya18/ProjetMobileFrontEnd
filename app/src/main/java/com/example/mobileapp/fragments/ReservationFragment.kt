package com.example.mobileapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.adapter.ReservationAdapter
import com.example.mobileapp.viewmodel.CarViewModel
import com.example.mobileapp.viewmodel.ReservationsViewModel
import androidx.lifecycle.Observer

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [ReservationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReservationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var adapter : ReservationAdapter
    lateinit var sharedPreferences : SharedPreferences
    lateinit var reservationsViewModel : ReservationsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reservation, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("app_state", Context.MODE_PRIVATE)
        reservationsViewModel = ViewModelProvider(this).get(ReservationsViewModel::class.java)

        val id = sharedPreferences.getString("id","DEFAULT")

        val recyclerView = view.findViewById<RecyclerView>(R.id.reservationsRecycler)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = ReservationAdapter(requireContext())
        recyclerView.adapter = adapter
        reservationsViewModel.getUserReservations(id!!)

        // add Observers
        // loading observer
        // Error message observer
        // List parking observer
        reservationsViewModel.reservations.observe(requireActivity(), Observer {  data ->
            adapter.setReservations(data)

        })

    }

}