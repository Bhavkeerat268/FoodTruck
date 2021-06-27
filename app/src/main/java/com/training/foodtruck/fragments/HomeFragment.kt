package com.training.foodtruck.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
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
import org.json.JSONException
import java.util.*
import java.util.Locale.filter
import kotlin.Comparator
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */

//Default Home fragment to show all the restaurants.
//It is the fragment shown when a user logs in
class HomeFragment : Fragment() {

    lateinit var recyclerHome: RecyclerView
    lateinit var recyclerAdaptor: HomeRecyclerAdaptor
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressBar: ProgressBar
    val RestaurantList = arrayListOf<Restaurant>()

    override fun onResume() {
        super.onResume()
        Toast.makeText(activity as Context, "On attach is ${RestaurantList.toString()}", Toast.LENGTH_SHORT).show()
    }


    var costComparator=Comparator<Restaurant>{res1,res2->        //Comparators to sort restaurants
        if(res1.restCostForOne.compareTo(res2.restCostForOne,true)==0)
        {
            res1.restName.compareTo(res2.restName,true)
        }
        else
        {
            res1.restCostForOne.compareTo(res2.restCostForOne,true)
        }
    }
    var ratingComparator=Comparator<Restaurant>{res1,res2->
        res1.restRating.compareTo(res2.restRating,true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        Toast.makeText(activity as Context, RestaurantList.toString(), Toast.LENGTH_SHORT).show()
        progressBar=view.findViewById(R.id.progressBarApp)
        progressBar.visibility=View.VISIBLE

        var searchBar:EditText=view.findViewById(R.id.search_bar_view)
        recyclerHome = view.findViewById(R.id.recyclerHome)
        layoutManager = LinearLayoutManager(activity)
        setHasOptionsMenu(true)
        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val queue = Volley.newRequestQueue(activity as Context)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                    val data=it.getJSONObject("data")
                    val success=data.getBoolean("success")
                    try {
                        if(success)
                        {
                            progressBar.visibility=View.GONE
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
                                        RestaurantList)

                                recyclerHome.adapter=recyclerAdaptor
                                recyclerHome.layoutManager=layoutManager

                            }
                        }
                        else
                        {
                            Toast.makeText(
                                activity as Context,
                                "Some unexpected error occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    catch (e:JSONException)
                    {
                        Toast.makeText(
                            activity as Context,
                            "Some unexpected error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
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
        searchBar.addTextChangedListener(object :TextWatcher{      //searchbar listener
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        return view
    }

    private fun filter(text: String) {                 //Function to implement search button functionality
        val filteredList= arrayListOf<Restaurant>()

        for(i in 0 until RestaurantList.size)
        {
            val filter=RestaurantList[i]
            if(filter.restName.toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(filter)
            }
        }
        recyclerAdaptor.filterList(filteredList)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.tolbar_widget,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==R.id.searchButton)
        {
            val dialog= androidx.appcompat.app.AlertDialog.Builder(activity as Context)
            dialog.setTitle("Sort by")
            val options= arrayOf("Cost(Low to High)","Cost(High To Low)","Rating")
            dialog.setSingleChoiceItems(options,-1)
            {dialog: DialogInterface?, which: Int ->
                when(which)
                {
                    0->{Collections.sort(RestaurantList,costComparator)

                        }
                    1->{                                                  //Sortingg of Restauranrts
                        Collections.sort(RestaurantList,costComparator)
                        RestaurantList.reverse()

                        }
                    2->{
                        Collections.sort(RestaurantList,ratingComparator)
                        RestaurantList.reverse()
                        }
                }
            }

            dialog.setPositiveButton("OK")
            {
                    text, _ ->
                 recyclerAdaptor.notifyDataSetChanged()

            }
            dialog.setNegativeButton("Cancel")
            {
                    text,listener->

            }
            dialog.create()
            dialog.show()
        }
        return super.onOptionsItemSelected(item)
    }
}
