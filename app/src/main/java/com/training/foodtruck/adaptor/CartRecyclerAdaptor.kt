package com.training.foodtruck.adaptor

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.training.foodtruck.R
import com.training.foodtruck.database.OrderDatabase
import com.training.foodtruck.database.OrderEntity
import com.training.foodtruck.model.TotalCost



class CartRecyclerAdaptor(
    var context: Context,
    var cartItems: List<OrderEntity>,
    var btnplaceOrder: Button
) :RecyclerView.Adapter<CartRecyclerAdaptor.CartViewHolder>() ,TotalCost{
    class CartViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val txtDishName:TextView=view.findViewById(R.id.cartDishname)
        val txtDishPrice:TextView=view.findViewById(R.id.cartDishPrice)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.single_row_recycler_cart,parent,false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {

        return cartItems.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        var cartItems=cartItems[position]
        holder.txtDishName.text=cartItems.dishName
        holder.txtDishPrice.text=cartItems.dishPrice
        btnplaceOrder.text="Place Order(Total:Rs.${TotalCost.DBClassAsync(context).execute().get().toString()})"

    }



}