package com.training.foodtruck.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.training.foodtruck.R
import com.training.foodtruck.model.FoodId


class OrderHistoryFoodIdListAdaptor(val context: Context,val foodIdList:List<FoodId>):RecyclerView.Adapter<OrderHistoryFoodIdListAdaptor.OrderFoodIdViewHolder>() {
    class OrderFoodIdViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val txtDishName:TextView=view.findViewById(R.id.orderDishName)
        val txtDishCost:TextView=view.findViewById(R.id.orderDishCost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderFoodIdViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.single_row_food_id_recycler_list,parent,false)
        return OrderFoodIdViewHolder(view)
    }


    override fun getItemCount(): Int {
        return foodIdList.size
    }

    override fun onBindViewHolder(holder: OrderFoodIdViewHolder, position: Int) {
        val foodId=foodIdList[position]
        holder.txtDishName.text=foodId.orderFoodName
        holder.txtDishCost.text="Rs. ${foodId.orderFoodCost}"
    }
}