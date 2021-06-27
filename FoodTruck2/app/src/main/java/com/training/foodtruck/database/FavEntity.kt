package com.training.foodtruck.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class FavEntity(
    @PrimaryKey val restaurantId:Int,
    @ColumnInfo(name = "restaurant_name")val restaurantName:String,
    @ColumnInfo(name = "restaurant_cost_for_one") val restaurantCostForOne:String,
    @ColumnInfo(name = "restaurant_image")val restaurantImage:String,
    @ColumnInfo(name = "restaurant_rating")val restaurantRating:String
)