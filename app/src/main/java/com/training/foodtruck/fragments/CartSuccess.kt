package com.training.foodtruck.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import com.training.foodtruck.R
import com.training.foodtruck.activity.CartActivity
import com.training.foodtruck.activity.Showcase

/**
 * A simple [Fragment] subclass.
 */


//Cart Fragment to display success message when order is succesfully placed
class CartSuccess : Fragment() {

    lateinit var btnOk:Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_cart_success, container, false)
        btnOk=view.findViewById(R.id.btnOk)
        btnOk.setOnClickListener {
            val intent=Intent(activity as Context,Showcase::class.java)
            startActivity(intent)
            activity?.finish()
        }
        return view
    }

}
