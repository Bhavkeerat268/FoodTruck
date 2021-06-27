package com.training.foodtruck.adaptor

import android.content.Context
import android.graphics.Color
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
import com.training.foodtruck.model.RestaurantDetailsMenu

class RestaurantDetailsRecyclerAdaptor(
    val context: Context,
    var resMenuDetails: ArrayList<RestaurantDetailsMenu>,
    val title: String?,
    val proceedToCart: Button
) :
    RecyclerView.Adapter<RestaurantDetailsRecyclerAdaptor.ResDetailsViewHolder>() {
    var orderList= arrayListOf<String>()


    class ResDetailsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtDishName:TextView=view.findViewById(R.id.txtDishname)
        val txtCostForOne:TextView=view.findViewById(R.id.txtCostForOne)
        val txtNumbering:TextView=view.findViewById(R.id.txtNumbering)
        val btnAddCart:Button=view.findViewById(R.id.btnAddCart)



    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResDetailsViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_details_single_row,parent,false)
        return ResDetailsViewHolder(view)
    }




    override fun getItemCount(): Int {
        return resMenuDetails.size

    }





    override fun onBindViewHolder(holder: ResDetailsViewHolder, position: Int) {
        val resMenu = resMenuDetails[position]
        holder.txtDishName.text = resMenu.resMenuName
        holder.txtCostForOne.text = resMenu.resMenuCostForOne
        holder.txtNumbering.text = "${position + 1}"

        val orderEntity = OrderEntity(
            resMenu.resMenuResId.toInt() ,
            title.toString(),
            resMenu.resMenuId.toInt(),
            resMenu.resMenuName.toString(),
            resMenu.resMenuCostForOne.toString()

        )

        holder.btnAddCart.setOnClickListener()
        {

            if(!orderList.contains(resMenu.resMenuId))
            {

                val async=DBAsyncTask(context,orderEntity,"add").execute()
                val result=async.get()
                if(result)
                {
                    holder.btnAddCart.setBackgroundColor(Color.parseColor("#000000"))
                    holder.btnAddCart.text = "Remove"
                    orderList.add(resMenu.resMenuId)


                }
            }
            else{
                val async=DBAsyncTask(context,orderEntity,"remove").execute()
                val result=async.get()
                if(result)
                {
                    holder.btnAddCart.setBackgroundColor(Color.parseColor("#d70f64"))
                    holder.btnAddCart.text = "Add"
                    orderList.remove(resMenu.resMenuId)


                }
            }
            proceed()



        }





    }
    fun proceed()
    {
        if(orderList.isEmpty())
        {
            proceedToCart.visibility=View.GONE
        }
        else{
            proceedToCart.visibility=View.VISIBLE
        }
    }

    class DBAsyncTask(val context: Context, val orderEntity: OrderEntity, val action: String) :
        AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, OrderDatabase::class.java, "order-db").build()



        override fun doInBackground(vararg p0: Void?): Boolean {

            when (action) {
                "add" -> {
                    db.orderDao().insertData(orderEntity)
                    println("data added is $orderEntity")
                    db.close()
                    return true
                }

                "remove" -> {
                    db.orderDao().deleteData(orderEntity)
                    println("data removed is $orderEntity")
                    db.close()
                    return true
                }
            }
            return false
        }
    }






        }








