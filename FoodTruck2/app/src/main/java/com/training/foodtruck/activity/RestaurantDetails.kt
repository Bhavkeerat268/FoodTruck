package com.training.foodtruck.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.training.foodtruck.R
import com.training.foodtruck.adaptor.RestaurantDetailsRecyclerAdaptor

import com.training.foodtruck.model.RestaurantDetailsMenu
import com.training.foodtruck.util.ConnectionManager

class RestaurantDetails : AppCompatActivity() {
    lateinit var recyclerResdetails: RecyclerView
    lateinit var recyclerAdaptor: RestaurantDetailsRecyclerAdaptor
    lateinit var layoutManager: LinearLayoutManager
    lateinit var toolbar: Toolbar
    var ResMenuDetails = arrayListOf<RestaurantDetailsMenu>()
    var resId:String?="100"
    var title:String?="title"
    lateinit var proceedToCart:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)
        resId=intent.getStringExtra("id")
        proceedToCart=findViewById(R.id.btnProceedToCart)
        proceedToCart.visibility= View.GONE


        recyclerResdetails = findViewById(R.id.recyclerResDetails)
        layoutManager = LinearLayoutManager(this@RestaurantDetails)
        toolbar=findViewById(R.id.toolbar)
        if (ConnectionManager().checkConnectivity(this)) {
            resId=intent.getStringExtra("id")
            title=intent.getStringExtra("name")
            setToolbar()
            val queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/$resId"
            val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                Response.Listener {
                    println("Response is $it")
                    val jsonResponse=it.getJSONObject("data")
                    val success=jsonResponse.getBoolean("success")
                    if(success)
                    {
                        val jsonResArray=jsonResponse.getJSONArray("data")
                        for(i in 0 until jsonResArray.length())
                        {
                            val jsonMenuObject=jsonResArray.getJSONObject(i)
                            var resMenuObject=RestaurantDetailsMenu(
                                jsonMenuObject.getString("id"),
                                jsonMenuObject.getString("name"),
                                jsonMenuObject.getString("cost_for_one"),
                                jsonMenuObject.getString("restaurant_id")
                            )
                            ResMenuDetails.add(resMenuObject)
                            recyclerAdaptor= RestaurantDetailsRecyclerAdaptor(this,ResMenuDetails,title,proceedToCart)
                            recyclerResdetails.adapter=recyclerAdaptor
                            recyclerResdetails.layoutManager=layoutManager

                        }



                    }

                },
                Response.ErrorListener {
                    Toast.makeText(this,"Some unexpected error occurred!!",Toast.LENGTH_SHORT).show()
                })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers=HashMap<String,String>()
                    headers["Content-type"]="application/json"
                    headers["token"]="f88a3e996dc76f"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)


        }
        else
        {
            val dialog=AlertDialog.Builder(this)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection not found")
            dialog.setPositiveButton("Turn On"){teext,listener->
                val settingsIntent=Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()

            }
            dialog.setNegativeButton("Exit"){ text,listener->
                ActivityCompat.finishAffinity(this)
            }
            dialog.create()
            dialog.show()
        }



    }
    fun setToolbar()
    {
        setSupportActionBar(toolbar)
        supportActionBar?.title=title
    }

    override fun onBackPressed() {
        deleteDatabase("order-db")
        super.onBackPressed()
    }





}

