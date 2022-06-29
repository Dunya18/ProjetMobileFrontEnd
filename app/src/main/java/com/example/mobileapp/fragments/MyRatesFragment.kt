package com.example.mobileapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.adapter.RateAdapter
import com.example.mobileapp.adapter.ReservationAdapter
import com.example.mobileapp.viewmodel.CarViewModel
import com.example.mobileapp.viewmodel.RateViewModel
import org.w3c.dom.Text


/**
 * A simple [Fragment] subclass.
 * Use the [MyRatesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyRatesFragment : Fragment() {

    lateinit var adapter: RateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_rates, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val parkingVM = ViewModelProvider(requireActivity()).get(CarViewModel::class.java)
        var parkingId = parkingVM.actualParking.value!!._id

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(requireContext())


        val vm = ViewModelProvider(this)[RateViewModel::class.java]
        vm.getRatedParking(parkingId)
        vm.loading.observe(viewLifecycleOwner) { loading ->
            if (loading == true) {
                view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            } else {
                view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
            }
        }
        vm.data.observe(this){ data ->
            if(data != null ) {
                recyclerView.layoutManager = layoutManager
                adapter = RateAdapter(requireContext())
                recyclerView.adapter = adapter
                adapter.setRates(data)
            }
        }






    }


}