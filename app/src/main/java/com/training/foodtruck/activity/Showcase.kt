package com.training.foodtruck.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.training.foodtruck.*
import com.training.foodtruck.fragments.*

//This is the main display activity of the application.
class Showcase : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinateLayout: CoordinatorLayout    // Variables decleations
    lateinit var toolbar: Toolbar
    lateinit var frame: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var sharedPreferences: SharedPreferences

    var previousitem: MenuItem? = null

    var user: String? = "User"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showcase)

        sharedPreferences = getSharedPreferences(
            getString(R.string.preference_file),
            Context.MODE_PRIVATE
        )

        //Selected shared preference file


        var name = sharedPreferences.getString(
            "name",
            "name"
        )  // User Name picked from Login Activity or Register Activity to display in navigation view






        drawerLayout = findViewById(R.id.drawerLayout)
        coordinateLayout = findViewById(R.id.coordinateLayout)
        toolbar =
            findViewById(R.id.toolbar)                         //Variables initialized with their IDs
        frame = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)


        val headerView = navigationView.getHeaderView(0)
        val navUsername: TextView =
            headerView.findViewById(R.id.navigationName) //Put username in navigation header
        navUsername.text = name

        setToolbar() //Toolbar set


        deleteDatabase("order-db")            //Order Database cleared after order


        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@Showcase,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )                                              //Action bar drawer toggle set


        drawerLayout.addDrawerListener(actionBarDrawerToggle)    //Listener Added to ACTIONBARDRAWERTOGGLE

        actionBarDrawerToggle.syncState()

        openHome()           //Home Fragment opened as default fragment for this activity


        navigationView.setNavigationItemSelectedListener {//Provided click listeners to items in navigation drawer


            if (previousitem != null) {
                previousitem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousitem = it
            when (it.itemId) {
                R.id.Home -> {
                    openHome()


                }




                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,        //Frame  replaced with the corresponding fragments when clicked and title is set
                            ProfileFragment()
                        )
                        .commit()
                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()
                }




                R.id.favouriteRestaurants -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,           //Frame  replaced with the corresponding fragments when clicked and title is set
                            FavouritesFragment()
                        )
                        .commit()
                    supportActionBar?.title = "Your favourite Restaurants"
                    drawerLayout.closeDrawers()
                }




                R.id.orderHistory -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, OrderHistoryFragment()) //Frame  replaced with the corresponding fragments when clicked and title is set
                        .commit()
                    supportActionBar?.setTitle("My Previous Orders")
                    drawerLayout.closeDrawers()
                }




                R.id.faq -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,  //Frame  replaced with the corresponding fragments when clicked and title is set
                            FaqFragment()
                        )
                        .commit()
                    supportActionBar?.title = "Frequently Asked Questions"
                    drawerLayout.closeDrawers()
                }




                R.id.logout -> {


                    val dialog = AlertDialog.Builder(this@Showcase)    //An alert dialog is set when clicked on logout item in navigation drawer
                    dialog.setTitle("Log Out")
                    dialog.setMessage("Are you sure you want to log out")
                    dialog.setPositiveButton("Yes") { text, listener ->
                        val intent = Intent(
                            this@Showcase,
                            LoginActivity::class.java
                        )


                        sharedPreferences = getSharedPreferences(
                            getString(R.string.preference_file),
                            Context.MODE_PRIVATE
                        )



                        sharedPreferences.edit().remove("name").apply()
                        sharedPreferences.edit().remove("email").apply()
                        sharedPreferences.edit().remove("password").apply()    //Shared preferences cleared after logging out
                        sharedPreferences.edit().remove("address").apply()
                        sharedPreferences.edit().remove("mobile_number").apply()
                        sharedPreferences.edit().putBoolean("IsLoggedIn", false).apply()
                        startActivity(intent)
                        finish()

                    }
                    dialog.setNegativeButton("No") { text, listener ->
                        navigationView.setCheckedItem(R.id.Home)

                    }
                    dialog.create()
                    dialog.show()
                }
            }

            return@setNavigationItemSelectedListener true
        }

    }




    fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "All Restaurants"      //Set toolbar
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }





    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)           //Making Drawer Toggle Active
        }
        return super.onOptionsItemSelected(item)
    }




    fun openHome() {
        supportActionBar?.title = "All Restaurants"
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.frame,                      //Setting Home Fragment as default display fragment for this activity
            HomeFragment()
        ).commit()
        drawerLayout.closeDrawers()
        navigationView.setCheckedItem(R.id.Home)

    }



    override fun onBackPressed() {

        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when (frag) {
            !is HomeFragment -> openHome()        //Handling back pressed functionality
            else -> super.onBackPressed()
        }

    }

}
