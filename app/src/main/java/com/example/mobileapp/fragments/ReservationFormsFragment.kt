package com.example.mobileapp.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.mobileapp.R
import com.example.mobileapp.viewmodel.CarViewModel
import com.example.mobileapp.viewmodel.ReservationsViewModel
import com.example.mobileapp.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [ReservationFormsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReservationFormsFragment : Fragment() {
    lateinit var vm_user : UserViewModel
    lateinit  var button_date: Button
    lateinit var textview_date: TextView
    lateinit  var button_hour_1: Button
    lateinit var textview_hour_1: TextView
    lateinit  var button_hour_2: Button
    lateinit var textview_hour_2: TextView
    lateinit var valider: Button
    lateinit var userID: String
    lateinit var parkingID: String
    lateinit var reservationsViewModel: ReservationsViewModel
    lateinit var DateEntree : Date
    lateinit var DateSortie: Date
    var cal = Calendar.getInstance()
    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         vm_user = ViewModelProvider(requireActivity())[UserViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_reservation_forms, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vm_parking = ViewModelProvider(requireActivity())[CarViewModel::class.java]
        sharedPreferences = requireActivity().getSharedPreferences("app_state", Context.MODE_PRIVATE)
        userID = sharedPreferences.getString("id","DEFAULT").toString()
      //view.findViewById<TextView>(R.id.userID).text = id

        parkingID = vm_parking.actualParking.value!!._id

        // get the references from layout file

        valider = view.findViewById<Button>(R.id.valider)

        textview_date = view.findViewById<TextView>(R.id.text_view_date_1)
         button_date = view.findViewById<Button>(R.id.button_date_1)

        textview_date.text = "----/--/--"

        textview_hour_1 = view.findViewById<TextView>(R.id.text_view_hour_1)
        button_hour_1 = view.findViewById<Button>(R.id.button_hour_1)

        textview_hour_1.text = "--:--"

        textview_hour_2 = view.findViewById<TextView>(R.id.text_view_hour_2)
        button_hour_2 = view.findViewById<Button>(R.id.button_hour_2)

        textview_hour_2.text = "--:--"
        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }
        // create OnTimeSetListner
        val timeSetListenerEntree = object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                updateHeureEntInView()
            }
        }

        val timeSetListenerSortie = object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                updateHeureSortieInView()
            }
        }


        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        button_date!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(requireContext(),
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        // when you click on the button, show TimeDialog that is set with OnDateSetListener
        button_hour_1!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {

                TimePickerDialog(requireContext(),
                    timeSetListenerEntree,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),true).show()
            }

        })
        button_hour_2!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {

                TimePickerDialog(requireContext(),
                    timeSetListenerSortie,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),true).show()
            }

        })


        valider.setOnClickListener {
            addReservation(view, textview_date.text.toString(), textview_hour_1.text.toString(),
                textview_hour_2.text.toString(), parkingID, userID)
        }
    }
    private fun updateDateInView() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date!!.text = sdf.format(cal.getTime())
    }

    private fun updateHeureEntInView() {
        val myFormat = "HH:mm" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_hour_1!!.text = sdf.format(cal.getTime())
    }
    private fun updateHeureSortieInView() {
        val myFormat = "HH:mm" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_hour_2!!.text = sdf.format(cal.getTime())
    }
    private fun   addReservation(view: View,date: String, Hour1: String, Hour2: String,
                                 parkingID: String, userID: String){
        reservationsViewModel = ViewModelProvider(requireActivity())[(ReservationsViewModel::class.java)]
        // transform string to date
        val myFormat = "yyyy-MM-dd HH:mm"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        DateEntree = SimpleDateFormat(myFormat).parse(date+" "+Hour1)
        DateSortie = SimpleDateFormat(myFormat).parse(date+" "+Hour2)
        if(isNetworkAvailable())
        reservationsViewModel.addReservation(DateEntree,DateSortie,parkingID,userID)
        reservationsViewModel.message.observe(this) { message ->
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }
        reservationsViewModel.created.observe(this) { created ->
            if (created) {
                Toast.makeText(requireContext(), "Reservation Added Succefully", Toast.LENGTH_LONG).show()
                view.findNavController()
                    .navigate(R.id.action_reservationFormsFragment_to_codeQRFragment)
            }
        }

    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}