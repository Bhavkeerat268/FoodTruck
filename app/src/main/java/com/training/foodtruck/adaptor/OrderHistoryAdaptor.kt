package com.training.foodtruck.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.training.foodtruck.R
import com.training.foodtruck.model.OrderHistoryData

class OrderHistoryAdaptor(
    val context: Context,
    val orderList: List<OrderHistoryData>,
    val imgCart: ImageView,
    var orderHead: TextView
):RecyclerView.Adapter<OrderHistoryAdaptor.OrderHistoryViewHolder>() {
    class OrderHistoryViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val txtorderResname:TextView=view.findViewById(R.id.orderResName)
        val txtorderDate:TextView=view.findViewById(R.id.orderDate)
        val recyclerlist:RecyclerView=view.findViewById(R.id.orderFoodIdList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_main_order_single_row,parent,false)
        return OrderHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {

        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        imgCart.visibility=View.GONE
        if(orderList.isEmpty())
        {
            val head="You have not placed any orders yet"
            orderHead.text=head
            imgCart.visibility=View.VISIBLE

        }
        else
        {
            val order=orderList[position]
            holder.txtorderResname.text=order.orderRestaurantname
            holder.txtorderDate.text=order.orderPlacedAt.substring(0,8)
            val foodIdList=order.orderFoodIdlist
            val layoutManager:RecyclerView.LayoutManager
            layoutManager=LinearLayoutManager(context)
            val orderHistoryFoodIdListAdaptor:OrderHistoryFoodIdListAdaptor
            orderHistoryFoodIdListAdaptor=OrderHistoryFoodIdListAdaptor(context,foodIdList)
            holder.recyclerlist.adapter=orderHistoryFoodIdListAdaptor
            holder.recyclerlist.layoutManager=layoutManager

        }

    }
}