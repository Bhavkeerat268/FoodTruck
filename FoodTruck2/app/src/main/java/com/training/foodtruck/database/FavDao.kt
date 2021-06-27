package com.training.foodtruck.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavDao {

    @Insert
    fun insertFav(favEntity: FavEntity)

    @Delete
    fun deletefav(favEntity: FavEntity)

    @Query("SELECT * FROM favourites")
    fun getAllFav():List<FavEntity>

    @Query("SELECT * FROM favourites WHERE restaurantId=:resId")
    fun getFavByID(resId:String):FavEntity
}