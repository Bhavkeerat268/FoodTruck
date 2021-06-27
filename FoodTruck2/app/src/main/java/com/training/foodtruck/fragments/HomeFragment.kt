package com.training.foodtruck.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.training.foodtruck.util.ConnectionManager
import com.training.foodtruck.adaptor.HomeRecyclerAdaptor
import com.training.foodtruck.R
import com.training.foodtruck.model.Restaurant

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    lateinit var recyclerHome: RecyclerView
    lateinit var recyclerAdaptor: HomeRecyclerAdaptor
    lateinit var layoutManager: RecyclerView.LayoutManager
    val RestaurantList = arrayListOf<Restaurant>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerHome = view.findViewById(R.id.recyclerHome)
        layoutManager = LinearLayoutManager(activity)
        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val queue = Volley.newRequestQueue(activity as Context)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                    val data=it.getJSONObject("data")
                    val success=data.getBoolean("success")
                    if(success)
                    {
                        val jsonArray=data.getJSONArray("data")
                        for(i in 0 until jsonArray.length())
                        {
                            val data=jsonArray.getJSONObject(i)
                            val ResObject=
                                Restaurant(
                                    data.getString("id"),
                                    data.getString("name"),
                                    data.getString("rating"),
                                    data.getString("cost_for_one"),
                                    data.getString("image_url")

                                )
                             RestaurantList.add(ResObject)
                            recyclerAdaptor=
                                HomeRecyclerAdaptor(
                                    activity as Context,
                                    RestaurantList
                                )
                            recyclerHome.adapter=recyclerAdaptor
                            recyclerHome.layoutManager=layoutManager

                        }
                    }



                }, Response.ErrorListener {
                    Toast.makeText(
                        activity as Context,
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
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Failed")
            dialog.setMessage("Internet Connection not found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()

        }
        return view
    }
}
