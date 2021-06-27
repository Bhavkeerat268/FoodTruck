package com.training.foodtruck.adaptor

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.training.foodtruck.R
import com.training.foodtruck.activity.RestaurantDetails
import com.training.foodtruck.database.FavEntity
import org.w3c.dom.Text

class FavouritesRecyclerAdaptor(val context: Context,val favList:List<FavEntity>): RecyclerView.Adapter<FavouritesRecyclerAdaptor.FavViewHolder>() {

    class FavViewHolder(view:View):RecyclerView.ViewHolder(view)
    {
        val txtFavResName:TextView=view.findViewById(R.id.txtFavResName)
        val txtFavCostForOne:TextView=view.findViewById(R.id.txtFavCostForOne)
        val imgFavResImage:ImageView=view.findViewById(R.id.imgFavResImage)
        val txtFavRating:TextView=view.findViewById(R.id.txtFavResRating)
        val llFavContent:LinearLayout=view.findViewById(R.id.llFavContent)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
       val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_favourites_single_row,parent,false)
        return FavViewHolder(view)
    }

    override fun getItemCount(): Int {
       return favList.size
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
       val favHolder=favList[position]
        holder.txtFavResName.text=favHolder.restaurantName
        holder.txtFavCostForOne.text=favHolder.restaurantCostForOne
        holder.txtFavRating.text=favHolder.restaurantRating
        Picasso.get().load(favHolder.restaurantImage).error(R.drawable.app_logo).into(holder.imgFavResImage)
        holder.llFavContent.setOnClickListener {
            val intent=Intent(context,RestaurantDetails::class.java)
            intent.putExtra("id",favHolder.restaurantId.toString())
            intent.putExtra("name",favHolder.restaurantName)
            context.startActivity(intent)
        }
    }


}