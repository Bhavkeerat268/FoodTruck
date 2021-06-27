package com.training.foodtruck.model

import com.google.gson.JsonObject
import org.json.JSONArray

data class OrderHistoryData(
    val orderId:String,
    val orderRestaurantname:String,
    val orderTotalCost:String,
    val orderPlacedAt:String,
    val orderFoodIdlist: ArrayList<FoodId>
)