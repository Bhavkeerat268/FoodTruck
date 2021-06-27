package com.training.foodtruck.activity

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.training.foodtruck.R
import com.training.foodtruck.adaptor.CartRecyclerAdaptor
import com.training.foodtruck.database.OrderDatabase
import com.training.foodtruck.database.OrderEntity
import com.training.foodtruck.fragments.CartSuccess
import com.training.foodtruck.model.TotalCost
import com.training.foodtruck.util.ConnectionManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class CartActivity : AppCompatActivity(), TotalCost {

    lateinit var recyclerCart: RecyclerView
    lateinit var recyclerCartAdaptor: CartRecyclerAdaptor
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var btnplaceOrder: Button                   //Variables declared
    lateinit var toolbar: Toolbar
    var cartItems = listOf<OrderEntity>()
    var restaurantName: String? = "Restaurant"
    lateinit var txtorderingFrom: TextView
    lateinit var frame: FrameLayout
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        frame = findViewById(R.id.framecart)
        toolbar = findViewById(R.id.toolbar)                    //Variables initialized
        recyclerCart = findViewById(R.id.recyclerCart)
        layoutManager = LinearLayoutManager(this)
        txtorderingFrom = findViewById(R.id.RestaurantName)
        restaurantName = intent.getStringExtra("ResName")
        txtorderingFrom.text = restaurantName
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        setToolbar()


        btnplaceOrder = findViewById(R.id.btnPlaceOrder)
        cartItems = DBgetCartItems(this).execute().get()
        recyclerCartAdaptor = CartRecyclerAdaptor(this, cartItems, btnplaceOrder)
        recyclerCart.adapter = recyclerCartAdaptor
        recyclerCart.layoutManager = layoutManager
        btnplaceOrder.setOnClickListener {


            val queue = Volley.newRequestQueue(this@CartActivity)
            val url = "http://13.235.250.119/v2/place_order/fetch_result/"
            var jsonParams = JSONObject()
            var userId = sharedPreferences.getString("user_id", "user_id")
            var restaurantId = intent.getStringExtra("ResId")
            var totalCost = TotalCost.DBClassAsync(this).execute().get().toString()

            jsonParams.put("user_id", userId)
            jsonParams.put("restaurant_id", restaurantId)
            jsonParams.put("total_cost", totalCost)
            var foodIdjsonArray = JSONArray()
            for (i in 0 until cartItems.size) {
                var id = cartItems.get(i)
                var foodId = id.Dish_id.toString()
                var foodObject = JSONObject()
                foodObject.put("food_item_id", foodId)
                foodIdjsonArray.put(foodObject)
            }
            println("data sent is : User_Id=$userId ,  Restaurant_Id=$restaurantId , Cost= $totalCost , food_Ids $foodIdjsonArray")

            jsonParams.put("food", foodIdjsonArray)


            if (ConnectionManager().checkConnectivity(this)) {



                var jsonObjectRequest =              //Json Request
                    object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                            println("cart response is $it")
                            var response = it.getJSONObject("data")
                            val result = response.getBoolean("success")
                            try {
                                if (result) {
                                    showNotification("Order Successfull","Your order has been placed!!")
                                    supportFragmentManager.beginTransaction()
                                        .replace(R.id.framecart, CartSuccess()).commit()

                                } else {
                                    Toast.makeText(
                                        this,
                                        "Some unexpected error occurred",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            } catch (e: JSONException) {
                                Toast.makeText(
                                    this,
                                    "Some unexpected error occurred",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        }, Response.ErrorListener {
                            Toast.makeText(
                                this,
                                "Some unexpected error occurred",
                                Toast.LENGTH_SHORT
                            ).show()

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
                dialog.setTitle("Failed")
                dialog.setMessage("Internet Connection not found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
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


    }
    fun showNotification(title: String, message: String) {
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel("YOUR_CHANNEL_ID",
                "YOUR_CHANNEL_NAME",
                NotificationManager.IMPORTANCE_HIGH)
            channel.description = "YOUR_NOTIFICATION_CHANNEL_DESCRIPTION"
            mNotificationManager.createNotificationChannel(channel)
        }
        val mBuilder = NotificationCompat.Builder(applicationContext, "YOUR_CHANNEL_ID")
            .setSmallIcon(R.mipmap.ic_launcher) // notification icon
            .setContentTitle(title) // title for notification
            .setContentText(message)// message for notification
            .setAutoCancel(true) // clear notification after click
        mNotificationManager.notify(0, mBuilder.build())
    }

    fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "My cart"                   //Toolbar set
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    class DBgetCartItems(context: Context) : AsyncTask<Void, Void, List<OrderEntity>>() {
        val db = Room.databaseBuilder(context, OrderDatabase::class.java, "order-db").build()
        override fun doInBackground(vararg params: Void?): List<OrderEntity> {
            println("Data recieved in cart is ${db.orderDao().getAllItems()}")
            return db.orderDao().getAllItems()                                   //All items shown from Database
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }                                                  //BAck button handled on toolbar
        return super.onOptionsItemSelected(item)
    }


}

