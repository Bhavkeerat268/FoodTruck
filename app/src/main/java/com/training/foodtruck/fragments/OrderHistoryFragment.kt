package com.training.foodtruck.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.training.foodtruck.R
import com.training.foodtruck.adaptor.OrderHistoryAdaptor
import com.training.foodtruck.model.FoodId
import com.training.foodtruck.model.OrderHistoryData

/**
 * A simple [Fragment] subclass.
 */
class OrderHistoryFragment : Fragment() {

    lateinit var recyclerMain:RecyclerView
    lateinit var orderHistoryAdaptor: OrderHistoryAdaptor
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var sharedPreferences: SharedPreferences
    lateinit var imgCart:ImageView
    lateinit var orderHead:TextView


    var orderList= arrayListOf<OrderHistoryData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_history, container, false)
        imgCart=view.findViewById(R.id.imgCart)
        imgCart.visibility=View.GONE
        recyclerMain=view.findViewById(R.id.recyclerMain)
        layoutManager=LinearLayoutManager(activity as Context)
        orderHead=view.findViewById(R.id.txtOrderHistoryHeading)


        sharedPreferences = activity!!.getSharedPreferences(
            getString(R.string.preference_file),
            Context.MODE_PRIVATE
        )
        val userId = sharedPreferences.getString("user_id", "UserId")
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/orders/fetch_result/$userId "

        println("User Id is $userId")
        val jsonObjectRequest =
            object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                println("Order History Response is $it")
                val jsonObject = it.getJSONObject("data")
                val result = jsonObject.getBoolean("success")
                if (result) {
                    val jsonArrray = jsonObject.getJSONArray("data")
                    for (i in 0 until jsonArrray.length()) {
                        val innerJsonObject = jsonArrray.getJSONObject(i)
                        val foodItems = innerJsonObject.getJSONArray("food_items")
                        val arrayOfFoodId = arrayListOf<FoodId>()
                        for (i in 0 until foodItems.length()) {
                            val innerFoodItemIdJsonObject = foodItems.getJSONObject(i)

                            val foodId = FoodId(
                                innerFoodItemIdJsonObject.getString("food_item_id"),
                                innerFoodItemIdJsonObject.getString("name"),
                                innerFoodItemIdJsonObject.getString("cost")
                            )
                           arrayOfFoodId.add(foodId)
                        }

                        val orderHistoryObject = OrderHistoryData(
                            innerJsonObject.getString("order_id"),
                            innerJsonObject.getString("restaurant_name"),
                            innerJsonObject.getString("total_cost"),
                            innerJsonObject.getString("order_placed_at"),
                            arrayOfFoodId


                        )
                        orderList.add(orderHistoryObject)
                        println("Order LIst sent is $orderList")
                        orderHistoryAdaptor=OrderHistoryAdaptor(activity as Context,orderList,imgCart,orderHead)
                        recyclerMain.adapter=orderHistoryAdaptor
                        recyclerMain.layoutManager=layoutManager

                        println(
                            "Order Data is ${innerJsonObject.getString("order_id")}   ${innerJsonObject.getString(
                                "restaurant_name"
                            )}   ${innerJsonObject.getString("total_cost")}   ${innerJsonObject.getString(
                                "order_placed_at"
                            )}    $arrayOfFoodId "
                        )

                        println("Order placed are $orderHistoryObject")

                    }
//
                }

            }, Response.ErrorListener { }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "f88a3e996dc76f"
                    return headers
                }
            }
        queue.add(jsonObjectRequest)
        return view
    }

}
