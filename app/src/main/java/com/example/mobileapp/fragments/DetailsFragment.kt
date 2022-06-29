package com.example.mobileapp.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.mobileapp.LatLng
import com.example.mobileapp.Parking
import com.example.mobileapp.R
import com.example.mobileapp.entity.Reservation
import com.example.mobileapp.retrofit.Endpoint
import com.example.mobileapp.viewmodel.CarViewModel
import com.google.android.gms.location.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
// private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    //  private var param1: Parking? = null
    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var userLocation: LatLng
    lateinit var parking: Parking
    lateinit var mContext: Context
    lateinit var timee: TextView
    lateinit var distancee: TextView
    lateinit var sharedPreferences : SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_details, container, false)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vm = ViewModelProvider(requireActivity())[CarViewModel::class.java]

        // get data here
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            // traitement de l’exception
//            viewModel.message.value = ""
            //  Log.d("hello-data", throwable.toString())

        }
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
//            viewModel.loading.value = true
            val response =
                Endpoint.createEndpoint().getParkingByID(requireArguments().getString("parking"))
            withContext(Dispatchers.Main) {
//                vm.loading.value = false


                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()
                    // here the adapter
                    vm.actualParking.value = data
                    if (data != null) {
                        this@DetailsFragment.parking = data

                        view.findViewById<TextView>(R.id.textView3).text = parking.nom
                        view.findViewById<TextView>(R.id.textView4).text = parking.commune
                        // set ouvert ou fermé
                        val rightNow = Calendar.getInstance()
                        val currentHourIn24Format: Int =
                            rightNow.get(Calendar.HOUR_OF_DAY) // return the hour in 24 hrs format (ranging from 0-23)
                        if (parking.horraireFerm!! > currentHourIn24Format) {
                            view.findViewById<TextView>(R.id.textView5).text = "Ouvert"
                            view.findViewById<TextView>(R.id.textView5)
                                .setTextColor(Color.parseColor("#0aad3f"))

                        } else {
                            view.findViewById<TextView>(R.id.textView5).text = "Fermé"
                            view.findViewById<TextView>(R.id.textView5)
                                .setTextColor(Color.parseColor("#FF0000"))
                        }

                        view.findViewById<TextView>(R.id.textView7).text =
                            parking.nbPlace?.let { (parking.reserved?.times(100))?.div(it).toString() } + "%"
                        view.findViewById<TextView>(R.id.textView10).text =
                            parking.horraireOuver.toString()
                        val Day: Int = rightNow.get(Calendar.DAY_OF_WEEK)
                        view.findViewById<TextView>(R.id.textView11).text = Day.toString()
                        distancee = view.findViewById<TextView>(R.id.textView8)
                        distancee.text = "loading .."
                        timee = view.findViewById<TextView>(R.id.textView14)
                        timee.text = "loading .."
                        view.findViewById<TextView>(R.id.textView12).text =
                            parking.horraireFerm.toString()

                        view.findViewById<TextView>(R.id.textView18).text = parking.tarifHeure
                        mFusedLocationClient =
                            LocationServices.getFusedLocationProviderClient(requireActivity())
                        getLastLocation(parking)

                        view.findViewById<View>(R.id.floatingActionButton).setOnClickListener {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=36.7050299,3.1739156&daddr=${parking.latitude},${parking.longitude}")
                            )
                            startActivity(intent)
                        }

                        val reserver = view.findViewById<Button>(R.id.reserverBtn)
                        reserver.setOnClickListener {
                            val isAuth =
                                view.context.getSharedPreferences("app_state", Context.MODE_PRIVATE)
                                    .getBoolean("is_authenticated", false)
                            if (!isAuth) {
                                Toast.makeText(
                                    view.context,
                                    "You're not connected",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {

                                view.findNavController()
                                    .navigate(R.id.action_detailsFragment_to_reservationFormsFragment)

                                // instanciate the database object
                                /* val appDatabase = AppDatabase.buildDatabase(view.context)

                                // get dao object
                                val reservationDao = appDatabase?.getReservationDao()

                                // create reservation object
                                val reservation = Reservation(
                                    0,
                                    "test@gmail.com",
                                    parking._id,
                                    LocalDateTime.now()
                                        .format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))
                                        .toString(),
                                )
                                reservationDao?.addReservation(reservation)*/

                                Toast.makeText(
                                    view.context,
                                    "Your reservation is successfuly added",
                                    Toast.LENGTH_LONG
                                ).show()


                            }
                        }
                    }
                } else {
//                    viewModel.message.value = "Une erreur s'est produit"
                }
            }
        }

        // rates
        val comments = view.findViewById<ImageButton>(R.id.imageButton)
        val rates = view.findViewById<ImageButton>(R.id.imageButton2)
        comments.setOnClickListener {
            sharedPreferences =
                requireActivity().getSharedPreferences("app_state", Context.MODE_PRIVATE)
            var auth = sharedPreferences.getBoolean("is_authenticated", false)
            if (!auth) {
                Toast.makeText(
                    view.context,
                    "You're not connected",
                    Toast.LENGTH_LONG
                ).show()
            } else {

                view.findNavController()
                    .navigate(R.id.action_detailsFragment_to_rateParkingFragment)
            }

        }
        rates.setOnClickListener {

            view.findNavController()
                .navigate(R.id.action_detailsFragment_to_myRatesFragment)

        }
    }


    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLastLocation(parking: Parking) {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                      //  this.userLocation = LatLng(location.latitude, location.longitude)
                        GlobalScope.launch(Dispatchers.IO) {

                            val apiUrl =
                                "https://api.tomtom.com/routing/1/calculateRoute/${parking.latitude},${parking.longitude}:36.7050299,3.1739156/json?key=DpdjVQkVpYACDku8sv1vTNUn3GAbp3Ur"
                            Log.d("URL : ", apiUrl)
                            val client = OkHttpClient().newBuilder()
                                .build()
                            val request: Request = Request.Builder()
                                .url(apiUrl)
                                .method("GET", null)
                                .build()

                            try {
                                val response = client.newCall(request).execute()
                                val body = response.body?.string()
                                val jsonObject = JSONTokener(body).nextValue() as JSONObject
                                if (body != null) {
                                   val routes = jsonObject.getJSONArray("routes")[0] as JSONObject
                                    val summary = routes.getJSONObject("summary")
                                    val time = summary.getDouble("travelTimeInSeconds")
                                    val distance = summary.getDouble("lengthInMeters")
                                    Thread {
                                        (mContext as Activity).runOnUiThread(java.lang.Runnable {
                                            timee.text =
                                                "${time / 60}min"
                                            distancee.text =
                                                "${distance / 1000}km"
                                        })
                                    }.start()
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(
                    requireActivity().getApplicationContext(),
                    "Turn on location",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        Looper.myLooper()?.let {
            mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                it
            )
        }
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation(parking)
            }
        }
    }


}