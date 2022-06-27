package com.example.mobileapp.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.mobileapp.MainActivity
import com.example.mobileapp.R
import com.example.mobileapp.viewmodel.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


const val RC_SIGN_IN = 123

class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters

    lateinit var sharedPreferences : SharedPreferences
    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var connect : TextView
    lateinit var userViewModel : UserViewModel
    lateinit var progressBar : ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_login, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // LOGIN WITH GOOGLE AND STUFF ..
        // SIGN IN WITH GOOGLE
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

        // Set the dimensions of the sign-in button.
        // Set the dimensions of the sign-in button.
        val signInButton = view.findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)

        // Set on click listner
        signInButton.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }



         email = view.findViewById<TextView>(R.id.email) as EditText
         password = view.findViewById<TextView>(R.id.password) as EditText
         connect = view.findViewById<TextView>(R.id.connect)
         progressBar = view.findViewById(R.id.progressBar2) as ProgressBar
        sharedPreferences = requireActivity().getSharedPreferences("app_state", Context.MODE_PRIVATE)

        connect.setOnClickListener{
            loginUser(view,email.text.toString(),password.text.toString())
           // view.findNavController()
             //   .navigate(R.id.action_loginFragment_to_mapFragment)
        }
        var create = view.findViewById<TextView>(R.id.create)
        create.setOnClickListener{

            // go to sign up fragment
            view.findNavController()
                .navigate(R.id.action_loginFragment_to_signUPFragment)

        }
    }
    // TYPICAL LOGIN
    private fun loginUser(view: View,txtEmail: String, txtPassword: String) {

        if (txtEmail.trim().isEmpty() || txtPassword.trim().isEmpty()){
            Toast.makeText(requireContext(), "please enter a valid email & password", Toast.LENGTH_SHORT).show()
        }
        else {
            userViewModel = ViewModelProvider(this)[(UserViewModel::class.java)]
            // login
            if(isNetworkAvailable())
            userViewModel.login(txtEmail,txtPassword)
            else{
                Log.d("offline","he is offline")
            }

            // add users data

            //  userViewModfel.user.postValue(User(id = "1"))
            // add observer
            userViewModel.loading.observe(this) { loading ->
                if (loading == true) {
                    // print("heredou2"+ userViewModel.user.value)
                    connect.visibility = View.GONE
                   // progressBar.visibility = View.VISIBLE
                } else {
                    connect.text = "connecter"
                   // progressBar.visibility = View.GONE
                    connect.visibility = View.VISIBLE

                }
            }
            userViewModel.message.observe(this) { message ->
                if (message != null) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                }
            }

            userViewModel.user.observe(this) { user ->
                if(user!=null) {
                    email.setText("")
                    password.setText("")
                    // enregistrer dans sharedPreferences le boolean is_authenticated
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("is_authenticated", true).apply()
                    editor.putString("email", txtEmail).apply()
                    editor.putString("id", user.id).apply()
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    //  finish()
                    startActivity(intent)
                }

            }

           /* userViewModel.isAuth.observe(this) { isAuth ->
                if (isAuth) {
                    email.setText("")
                    password.setText("")
                    print("plz"+userViewModel.user.value)
                    // enregistrer dans sharedPreferences le boolean is_authenticated
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("is_authenticated", true).apply()
                    editor.putString("email", txtEmail).apply()

                   view.findNavController()
                        .navigate(R.id.action_loginFragment_to_carFragment)
                    // go to reservation
                  //  val intent = Intent(requireContext(), ReservationActivity::class.java)
                    //  finish()
                    //startActivity(intent)
                }
            }*/



        }
    }

    // LOGIN WITH GOOGLE
    // for sign in with google
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
            if (acct != null) {
                val personName = acct.displayName
                val personGivenName = acct.givenName
                val personFamilyName = acct.familyName
                val personEmail = acct.email
                email.setText(personEmail)
                val personId = acct.id
                val personPhoto: Uri? = acct.photoUrl
            }
            // enregistrer dans sharedPreferences le boolean is_authenticated
            val editor = sharedPreferences.edit()
            editor.putBoolean("is_authenticated", true).apply()
            editor.putString("email", acct?.email).apply()

            // go to reservation
         //   val intent = Intent(this.requireContext(), ReservationActivity::class.java)
            //finish()
           // startActivity(intent)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
          //  val intent = Intent(this.requireContext(), SignUpActivity::class.java)
            //  finish()
            //startActivity(intent)
        }
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


}