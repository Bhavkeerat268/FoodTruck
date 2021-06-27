package com.training.foodtruck.model

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.training.foodtruck.database.OrderDatabase

interface TotalCost {
    class DBClassAsync(val context: Context): AsyncTask<Void, Void, Int>()
    {
        override fun doInBackground(vararg params: Void?): Int {
            val db= Room.databaseBuilder(context, OrderDatabase::class.java,"order-db").build()
            return db.orderDao().totalCost()
        }

    }
}