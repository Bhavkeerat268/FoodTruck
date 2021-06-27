package com.training.foodtruck.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.training.foodtruck.*
import com.training.foodtruck.fragments.FaqFragment
import com.training.foodtruck.fragments.FavouritesFragment
import com.training.foodtruck.fragments.HomeFragment
import com.training.foodtruck.fragments.ProfileFragment

class Showcase : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinateLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frame: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var sharedPreferences: SharedPreferences
     var previousitem:MenuItem?=null
    var user:String?="User"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showcase)



        drawerLayout = findViewById(R.id.drawerLayout)
        coordinateLayout = findViewById(R.id.coordinateLayout)
        toolbar = findViewById(R.id.toolbar)
        frame = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)
        setToolbar()
        deleteDatabase("order-db")
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@Showcase,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        openHome()
        navigationView.setNavigationItemSelectedListener {
            if(previousitem!=null)
            {
                previousitem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousitem=it
            when (it.itemId) {
                R.id.Home -> {openHome()


                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            ProfileFragment()
                        )
                        .commit()
                    supportActionBar?.title="Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.favouriteRestaurants -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FavouritesFragment()
                        )
                        .commit()
                    supportActionBar?.title="Your favourite Restaurants"
                    drawerLayout.closeDrawers()
                }
                R.id.faq ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FaqFragment()
                        )
                        .commit()
                    supportActionBar?.title="Frequently Asked Questions"
                    drawerLayout.closeDrawers()
                }
                R.id.logout ->{
                    val dialog=AlertDialog.Builder(this@Showcase)
                    dialog.setTitle("Log Out")
                    dialog.setMessage("Are you sure you want to log out")
                    dialog.setPositiveButton("Yes"){text,listener->
                        val intent=Intent(this@Showcase,
                            LoginActivity::class.java)
                        sharedPreferences=getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
                        sharedPreferences.edit().putBoolean("IsLoggedIn",false).apply()
                        startActivity(intent)

                    }
                    dialog.setNegativeButton("No"){text,listener->

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
        supportActionBar?.title="All Restaurants"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    fun openHome()
    {
        supportActionBar?.title="All Restaurants"
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.frame,
            HomeFragment()
        ).commit()
        drawerLayout.closeDrawers()
        navigationView.setCheckedItem(R.id.Home)

    }
    override fun onBackPressed()
    {

        val frag=supportFragmentManager.findFragmentById(R.id.frame)
        when(frag)
        {
            !is HomeFragment ->openHome()
            else->super.onBackPressed()
        }

    }

}
