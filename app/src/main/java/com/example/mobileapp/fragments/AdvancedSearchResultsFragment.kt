package com.example.mobileapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.adapter.SearchAdapter
import com.example.mobileapp.viewmodel.SearchViewModel
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [AdvancedSearchResultsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdvancedSearchResultsFragment : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_advanced_search_results, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        val searchViewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]


            if (searchViewModel.loading.value == true) {
                view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            } else {
                view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
            }


            recyclerView.layoutManager = layoutManager
        if(searchViewModel.advancedSearchList.value != null) {
            val adapter = SearchAdapter(view.context, searchViewModel.advancedSearchList.value!!)
            recyclerView.adapter = adapter
        }
        else{
            Log.d("results", " no results ")
        }

    }
}