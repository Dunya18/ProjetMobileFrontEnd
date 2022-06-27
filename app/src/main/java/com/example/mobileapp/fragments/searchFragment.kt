package com.example.mobileapp.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.adapter.SearchAdapter
import com.example.mobileapp.viewmodel.SearchViewModel
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [searchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class searchFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        val searchBar = view.findViewById<SearchView>(R.id.search)
        val searchViewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]

        searchBar.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if(isNetworkAvailable())
                    searchViewModel.searchByNom(query.trim().lowercase(Locale.getDefault()))
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })

        searchViewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading == true) {
                view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            } else {
                view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
            }
        }
        searchViewModel.searchList.observe(viewLifecycleOwner) {
                searchList ->
            recyclerView.layoutManager = layoutManager
            val adapter = SearchAdapter(view.context, searchList)
            recyclerView.adapter = adapter
        }
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}