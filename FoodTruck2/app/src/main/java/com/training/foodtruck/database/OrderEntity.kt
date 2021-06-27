package com.training.foodtruck.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order1")
data class OrderEntity(
    @ColumnInfo(name = "restaurant_id")  val restaurant_id: Int,
    @ColumnInfo(name = "restaurant_name") val restaurantName: String,
    @PrimaryKey val Dish_id: Int,
    @ColumnInfo(name = "dish_name") val dishName: String,
    @ColumnInfo(name = "dish_price") val dishPrice: String
)