package com.training.foodtruck.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.training.foodtruck.R

/**
 * A simple [Fragment] subclass.
 */


//Fragment to show user details
class ProfileFragment : Fragment() {
    lateinit var txtProfileName: TextView
    lateinit var txtProfileEmail: TextView
    lateinit var txtProfileNumber: TextView
    lateinit var txtProfileAddress: TextView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        txtProfileName = view.findViewById(R.id.txtProfileName)
        txtProfileEmail = view.findViewById(R.id.txtProfileEmail)
        txtProfileNumber = view.findViewById(R.id.txtProfileNumber)
        txtProfileAddress = view.findViewById(R.id.txtProfileAddress)
        sharedPreferences = activity!!.getSharedPreferences(
            getString(R.string.preference_file),
            Context.MODE_PRIVATE
        )
        txtProfileName.text = sharedPreferences.getString("name", "Name")
        txtProfileEmail.text = sharedPreferences.getString("email", "Email")//Values accessed from shared preferences of login or register activity
        txtProfileAddress.text = sharedPreferences.getString("address", "Address")
        txtProfileNumber.text = sharedPreferences.getString("mobile_number", "Mobile Number")


        return view
    }

}
