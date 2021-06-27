package com.training.foodtruck.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.training.foodtruck.R
import com.training.foodtruck.adaptor.RestaurantDetailsRecyclerAdaptor
import com.training.foodtruck.database.OrderDatabase
import com.training.foodtruck.database.OrderEntity

import com.training.foodtruck.model.RestaurantDetailsMenu
import com.training.foodtruck.util.ConnectionManager
import org.json.JSONException

class RestaurantDetails : AppCompatActivity() {
    lateinit var recyclerResdetails: RecyclerView
    lateinit var recyclerAdaptor: RestaurantDetailsRecyclerAdaptor   //Variables Declared
    lateinit var layoutManager: LinearLayoutManager
    lateinit var toolbar: Toolbar
    var ResMenuDetails = arrayListOf<RestaurantDetailsMenu>()
    var resId: String? = "100"
    var title: String? = "title"
    lateinit var progressBar: ProgressBar
    lateinit var proceedToCart: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)
        progressBar=findViewById(R.id.progressBarApp)
        progressBar.visibility=View.VISIBLE
        resId = intent.getStringExtra("id")                        //Variables Initialized
        proceedToCart = findViewById(R.id.btnProceedToCart)
        proceedToCart.visibility = View.GONE
        recyclerResdetails = findViewById(R.id.recyclerResDetails)
        layoutManager = LinearLayoutManager(this@RestaurantDetails)
        toolbar = findViewById(R.id.toolbar)

        resId = intent.getStringExtra("id")
        title = intent.getStringExtra("name")



        setToolbar()                            //Toolbar set
        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$resId"


        if (ConnectionManager().checkConnectivity(this)) {   //Checked Internet Connection

            val jsonObjectRequest = object : JsonObjectRequest(   //Json Request Made
                Request.Method.GET,
                url,
                null,
                Response.Listener {
                    println("Response is $it")
                    val jsonResponse = it.getJSONObject("data")
                    val success = jsonResponse.getBoolean("success")

                    try {
                        if (success) {
                            progressBar.visibility=View.GONE
                            val jsonResArray = jsonResponse.getJSONArray("data")
                            for (i in 0 until jsonResArray.length()) {
                                val jsonMenuObject = jsonResArray.getJSONObject(i)
                                var resMenuObject = RestaurantDetailsMenu(
                                    jsonMenuObject.getString("id"),
                                    jsonMenuObject.getString("name"),
                                    jsonMenuObject.getString("cost_for_one"),
                                    jsonMenuObject.getString("restaurant_id")
                                )
                                ResMenuDetails.add(resMenuObject)
                                recyclerAdaptor = RestaurantDetailsRecyclerAdaptor(
                                    this,
                                    ResMenuDetails,
                                    title,
                                    proceedToCart
                                )
                                recyclerResdetails.adapter = recyclerAdaptor
                                recyclerResdetails.layoutManager = layoutManager

                            }


                        }
                        else
                        {
                            Toast.makeText(this, "Some unexpected error occurred!!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    catch (e:JSONException)
                    {
                        Toast.makeText(this, "Some unexpected error occurred!!", Toast.LENGTH_SHORT)
                            .show()

                    }


                },
                Response.ErrorListener {
                    Toast.makeText(this, "Some unexpected error occurred!!", Toast.LENGTH_SHORT)
                        .show()
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "f88a3e996dc76f"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)


        } else {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection not found")
            dialog.setPositiveButton("Turn On") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()

            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(this)
            }
            dialog.create()
            dialog.show()
        }


    }

    fun setToolbar() {
        setSupportActionBar(toolbar)            //Toolbar function made
        supportActionBar?.title = title
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {                      //Made back button active
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val dialog=AlertDialog.Builder(this)
        dialog.setTitle("Are You sure you want to go back?")
        dialog.setMessage("All the items added will be removed.")
        dialog.setPositiveButton("Yes")
        {
            text,listener->
            deleteDatabase("order-db")
            finish()//Handled back button
        }
        dialog.setNegativeButton("No")
        {
            text,listener->
        }
        dialog.create()
        dialog.show()

    }
}

