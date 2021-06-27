package com.training.foodtruck.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderDao {
    @Insert
    fun insertData(orderEntity: OrderEntity)

    @Delete
    fun deleteData(orderEntity: OrderEntity)

    @Query("SELECT * FROM order1")
    fun getAllItems(): List<OrderEntity>

    @Query("SELECT * FROM order1 WHERE Dish_id=:dishId")
    fun getByID(dishId:String):OrderEntity

    @Query("SELECT SUM(dish_price) FROM order1")
    fun totalCost():Int
}