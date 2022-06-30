package com.example.mobileapp.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.mobileapp.MainActivity
import com.example.mobileapp.R
import com.example.mobileapp.viewmodel.UserViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [SignUPFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUPFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var sharedPreferences : SharedPreferences
    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var name : EditText
    lateinit var family_name : EditText
    lateinit var phone_number : EditText
    lateinit var userViewModel : UserViewModel
    lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_u_p, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = view.findViewById<TextView>(R.id.email1) as EditText
        password = view.findViewById<TextView>(R.id.password2) as EditText
        family_name = view.findViewById<TextView>(R.id.familyname) as EditText
        phone_number = view.findViewById<TextView>(R.id.phonenumber) as EditText
        name = view.findViewById<TextView>(R.id.name) as EditText
        var signup = view.findViewById<TextView>(R.id.signup)
        sharedPreferences = requireActivity().getSharedPreferences("app_state", Context.MODE_PRIVATE)

        signup.setOnClickListener {
            if(checkEmail(email.text.toString()))
            signup(name.text.toString(),family_name.text.toString(),phone_number.text.toString(),email.text.toString(),password.text.toString())
            else
                Toast.makeText(requireContext(), "please enter a valid email", Toast.LENGTH_LONG).show()

        }
    }
    private fun signup(nametxt: String, family_nametxt: String, phone_numbertxt: String, emailtxt: String, passwordtxt: String) {

        if (emailtxt.trim().isEmpty() || passwordtxt.trim().isEmpty()){
            Toast.makeText(requireContext(), "please enter a valid email & password", Toast.LENGTH_SHORT).show()
        }
        else {

            userViewModel = ViewModelProvider(this)[(UserViewModel::class.java)]
            // signup
            userViewModel.signup(nametxt,family_nametxt,phone_numbertxt,emailtxt,passwordtxt)
            // add users data
            // userViewModel.user.postValue(User(id = "1"))
            // add observer
            userViewModel.loading.observe(this) { loading ->
                if (loading == true) {

                    // signup.visibility = View.GONE
                    //progressBar.visibility = View.VISIBLE
                } else {
                    // signup.text = "enregistrer"
                    //    progressBar.visibility = View.GONE
                    //  signup.visibility = View.VISIBLE

                }
            }
            userViewModel.message.observe(this) { message ->
                if (message != null) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                }
            }
            userViewModel.isAuth.observe(this) { isAuth ->
                if (isAuth) {
                    email.setText("")
                    password.setText("")

                    // enregistrer dans sharedPreferences le boolean is_authenticated
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("is_authenticated", true).apply()
                    editor.putString("email", emailtxt).apply()

                    // go to reservation
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                }
            }



        }
    }
    private fun checkEmail(email: String): Boolean {
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
            return EMAIL_REGEX.toRegex().matches(email);
    }

}