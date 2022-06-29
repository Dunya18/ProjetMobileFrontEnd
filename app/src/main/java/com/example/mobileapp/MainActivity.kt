package com.example.mobileapp


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.mobileapp.databinding.ActivityMainBinding
import com.example.mobileapp.fragments.CarFragment
import com.example.mobileapp.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    lateinit var userViewModel: UserViewModel
    var isAuth : Boolean = false
    lateinit var sharedPreferences : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        userViewModel = ViewModelProvider(this)[(UserViewModel::class.java)]


        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val navHostFragment = supportFragmentManager. findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navBottom,navController)
        // Change the title in the action bar
        getActionBar()?.setTitle("My Parking");
        getSupportActionBar()?.setTitle("My Parking");
        // observers
        userViewModel.user.observe(this, Observer { user ->
            if(user!=null) {
                val intent = Intent(this, CarFragment::class.java)
                startActivity(intent)
                invalidateOptionsMenu()
            }

        })



    }



    override fun onSupportNavigateUp() = navController.navigateUp() || super.onSupportNavigateUp()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.overflow_menu, menu)
        sharedPreferences = this.getSharedPreferences("app_state", Context.MODE_PRIVATE)
        isAuth = sharedPreferences.getBoolean("is_authenticated",false)
        if(isAuth) {
            menu?.getItem(0)?.setVisible(true)
        }
        else {
            menu?.getItem(0)?.setVisible(false)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.logout-> {
                val editor = this.getSharedPreferences("app_state", Context.MODE_PRIVATE).edit()
                editor.remove("is_authenticated")
                editor.apply()
                userViewModel.user.postValue(null)

                // instanciate the database object
                val appDatabase = AppDatabase.buildDatabase(this)
                // get dao object
                val reservationDao = appDatabase?.getReservationDao()
                //delete
                reservationDao?.deleteUserReservation()
                // go to home
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }
        return  true

    }

}
