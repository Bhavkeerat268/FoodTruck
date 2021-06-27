package com.training.foodtruck.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.training.foodtruck.R
import com.training.foodtruck.adaptor.FavouritesRecyclerAdaptor
import com.training.foodtruck.database.FavDatabase
import com.training.foodtruck.database.FavEntity
import com.training.foodtruck.util.ConnectionManager

/**
 * A simple [Fragment] subclass.
 */
class FavouritesFragment : Fragment() {
    lateinit var recyclerAdaptor: FavouritesRecyclerAdaptor
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerFavourtes: RecyclerView
    var dbfavList = listOf<FavEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.favourites_fragment, container, false)
        recyclerFavourtes=view.findViewById(R.id.recyclerFav)
        layoutManager=LinearLayoutManager(activity as Context)


            dbfavList=DbGetfavourites(activity as Context).execute().get()
        recyclerAdaptor= FavouritesRecyclerAdaptor(activity as Context,dbfavList)
            recyclerFavourtes.adapter=recyclerAdaptor
            recyclerFavourtes.layoutManager=layoutManager







        return view
    }

    class DbGetfavourites(val context: Context) : AsyncTask<Void, Void, List<FavEntity>>() {
        override fun doInBackground(vararg params: Void?): List<FavEntity> {

            var db = Room.databaseBuilder(context, FavDatabase::class.java, "fav-db").build()
            println(db.favDao().getAllFav())
            return db.favDao().getAllFav()


        }


    }
}
