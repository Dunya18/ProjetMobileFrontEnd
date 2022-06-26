package com.example.mobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mobileapp.viewmodel.CarViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
class MapActivity : AppCompatActivity(),
    GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    lateinit var carViewModel: CarViewModel
    private var markerParking: Marker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        // Change the title in the action bar
        getActionBar()?.setTitle("Nos parkings vu sur map");
        getSupportActionBar()?.setTitle("Nos parkings vu sur map");
    }
    /** Called when the map is ready.  */
    override fun onMapReady(map: GoogleMap) {

        // initaliser carviewmodel
        carViewModel = ViewModelProvider(this).get(CarViewModel::class.java)
        // recuperer les parkings
        carViewModel.loadParkings()
        // display sur maps
        carViewModel.data.observe( this, { data ->
            if(data !=null){
                var lat: Double
                var long: Double
                var parking : LatLng
                for(item in data ){
                    lat = item.latitude!!.toDouble()
                    long = item.longitude!!.toDouble()
                    parking = LatLng(lat,long)
                    markerParking = map.addMarker(
                        MarkerOptions()
                            .position(parking)
                            .title(item.nom)
                    )
                }
                // move the camera to the last item

                var i = data.size - 1
                lat = data.get(i).latitude!!.toDouble()
                long = data.get(i).longitude!!.toDouble()
                parking = LatLng(lat,long)
                map.moveCamera(CameraUpdateFactory.newLatLng(parking))
            }
            else{
                Log.d(" no parking available ", "no parking available")
            }
        } )



        // Add some markers to the map, and add a data object to each marker.

      /*  markerPerth = map.addMarker(
            MarkerOptions()
                .position(PERTH)
                .title("Perth")
        )
        markerPerth = map.addMarker(
            MarkerOptions()
                .position(PERTH)
                .title("Perth")
        )
        markerPerth?.tag = 0
        markerSydney = map.addMarker(
            MarkerOptions()
                .position(SYDNEY)
                .title("Sydney")
        )
        markerSydney?.tag = 0
        markerBrisbane = map.addMarker(
            MarkerOptions()
                .position(BRISBANE)
                .title("Brisbane")
        )
        markerBrisbane?.tag = 0 */

        // Set the camera


        // Set a listener for marker click.
        map.setOnMarkerClickListener(this)
    }

    /** Called when the user clicks a marker.  */
    override fun onMarkerClick(marker: Marker): Boolean {

        // Retrieve the data from the marker.
        val clickCount = marker.tag as? Int

        // Check if a click count was set, then display the click count.
        clickCount?.let {
            val newClickCount = it + 1
            marker.tag = newClickCount
            Toast.makeText(
                this,
                "${marker.title} has been clicked $newClickCount times.",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false
    }
}