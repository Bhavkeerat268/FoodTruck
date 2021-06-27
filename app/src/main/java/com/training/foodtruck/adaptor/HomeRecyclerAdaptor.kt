package com.training.foodtruck.adaptor

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.squareup.picasso.Picasso
import com.training.foodtruck.R
import com.training.foodtruck.activity.RestaurantDetails
import com.training.foodtruck.database.FavDatabase
import com.training.foodtruck.database.FavEntity
import com.training.foodtruck.model.Restaurant


class HomeRecyclerAdaptor(
    val context: Context,
    var RestaurantList: ArrayList<Restaurant>
) :
    RecyclerView.Adapter<HomeRecyclerAdaptor.HomeViewHolder>() {
    val favList= arrayListOf<String>()

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtResName: TextView = view.findViewById(R.id.txtResName)
        val txtResCostForOne: TextView = view.findViewById(R.id.txtCostForOne)
        val imgResImage: ImageView = view.findViewById(R.id.imgResImage)
        val txtResRating: TextView = view.findViewById(R.id.txtResRating)
        val llcontent: LinearLayout = view.findViewById(R.id.llcontent)
        val favBtn:ImageView=view.findViewById(R.id.imgFavBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {


        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_home_single_row, parent, false)
        return HomeViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return RestaurantList.size

    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurant = RestaurantList[position]
        holder.txtResName.text = restaurant.restName
        holder.txtResCostForOne.text = "${restaurant.restCostForOne}/person"
        holder.txtResRating.text = restaurant.restRating
        Picasso.get().load(restaurant.restImage).error(R.drawable.app_logo).into(holder.imgResImage)
        holder.llcontent.setOnClickListener(){
            val intent=Intent(context,RestaurantDetails::class.java)
            intent.putExtra("id",restaurant.restId)
            intent.putExtra("name",restaurant.restName)
            context.startActivity(intent)
        }
        val favEntity=FavEntity(
            restaurant.restId.toInt(),
            restaurant.restName,
            restaurant.restCostForOne,
            restaurant.restImage,
            restaurant.restRating

        )
        var isFav=DbFavAsync(context,favEntity,3).execute().get()
        holder.favBtn.setOnClickListener()
        {


            if(!isFav)
            {
                val async=DbFavAsync(context,favEntity,1).execute()


                val result=async.get()
                if(result)
                {
                    Toast.makeText(context,"Added to favourites",Toast.LENGTH_SHORT).show()
                    favList.add(restaurant.restId)
                    holder.favBtn.setImageResource(R.drawable.ic_fav_clicked)
                    isFav=true
                }
                else
                {
                    Toast.makeText(context,"Some error occurred",Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                val async=DbFavAsync(context,favEntity,2).execute()
                val result=async.get()
                if(result)
                {
                    Toast.makeText(context,"Removed from favourites",Toast.LENGTH_SHORT).show()
                    favList.remove(restaurant.restId)
                    holder.favBtn.setImageResource(R.drawable.ic_favourites_unclicked)
                    isFav=false
                }
                else
                {
                    Toast.makeText(context,"Some error occurred",Toast.LENGTH_SHORT).show()
                }

            }




        }
        if(isFav)
        {
            holder.favBtn.setImageResource(R.drawable.ic_fav_clicked)
        }
        else
        {
            holder.favBtn.setImageResource(R.drawable.ic_favourites_unclicked)
        }




    }

    fun filterList(filteredList: ArrayList<Restaurant>) {
        RestaurantList=filteredList
        notifyDataSetChanged()
    }


    class DbFavAsync(val context: Context,val favouriteEntity: FavEntity,val mode:Int):AsyncTask<Void,Void,Boolean>()
     {
         val db=Room.databaseBuilder(context,FavDatabase::class.java,"fav-db").build()
         override fun doInBackground(vararg params: Void?): Boolean {
            when(mode)
            {
                1->{
                    db.favDao().insertFav(favouriteEntity)
                    println("Fav data added is $favouriteEntity")
                    db.close()
                    return true
                    }
                2->{db.favDao().deletefav(favouriteEntity)
                    println("Fav data removed is $favouriteEntity")
                    db.close()
                    return true
                    }
                3->{
                    val favID:FavEntity?=db.favDao().getFavByID(favouriteEntity.restaurantId.toString())
                    db.close()
                    return favID!=null

                    }


            }
             return false

         }

     }
}