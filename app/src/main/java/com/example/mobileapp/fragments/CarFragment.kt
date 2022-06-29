package com.example.mobileapp.fragments

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.MapActivity
import com.example.mobileapp.R
import com.example.mobileapp.adapter.ParkingAdapter
import com.example.mobileapp.viewmodel.CarViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CarFragment : Fragment(){
    lateinit var carViewModel: CarViewModel
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ParkingAdapter
    companion object {
        fun newInstance() = CarFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.car_fragment, container, false)
        return view
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        progressBar = itemView.findViewById(R.id.progressBar) as ProgressBar
        carViewModel = ViewModelProvider(this).get(CarViewModel::class.java)

        // TODO: Use the ViewModel
        recyclerView = itemView.findViewById(R.id.recyclerView) as RecyclerView
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        adapter = ParkingAdapter(requireActivity())
            recyclerView.adapter = adapter
        if(isNetworkAvailable())
        carViewModel.loadParkings()
        else{
            Log.d("offline","he is offline")
        }
            // add Observers
            // loading observer
        carViewModel.loading.observe(requireActivity(), Observer {  loading->
                if(loading) {
                progressBar.visibility = View.VISIBLE
                }
                else {
              progressBar.visibility = View.GONE
                }

            })
            // Error message observer
        carViewModel.errorMessage.observe(requireActivity(), Observer {  message ->
                //Toast.makeText(requireContext(),"Une erreur s'est produite",Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()


            })
            // List parking observer
        carViewModel.data.observe(requireActivity(), Observer {  data ->
            adapter.setParkings(data)

            })

        // Go to maps
        val maps = itemView.findViewById<FloatingActionButton>(R.id.maps)
        maps.setOnClickListener {
            val intent = Intent(requireContext(), MapActivity::class.java)
            startActivity(intent)
        }

        }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    }


