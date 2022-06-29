package com.example.mobileapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.mobileapp.Parking
import com.example.mobileapp.R
import com.example.mobileapp.viewmodel.SearchViewModel
import org.w3c.dom.Text


/**
 * A simple [Fragment] subclass.
 * Use the [AdvancedSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdvancedSearchFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_advanced_search, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val destination = view.findViewById<TextView>(R.id.destination) as EditText
        val maxprice = view.findViewById<TextView>(R.id.maxprice) as EditText
        val distance = view.findViewById<TextView>(R.id.distance) as EditText
        val search = view.findViewById<ImageButton>(R.id.searchButton)

        val searchViewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
        search.setOnClickListener {

            searchViewModel.advancedSearch(destination.text.toString(),Integer.parseInt(maxprice.text.toString().trim()),Integer.parseInt(distance.text.toString().trim()))
            searchViewModel.advancedSearchList.observe(this) { data ->
                if (data != null) {
                    view.findNavController()
                        .navigate(R.id.action_advancedSearchFragment_to_advancedSearchResultsFragment)
                    }
                }
            }
    }
}



