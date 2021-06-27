package com.training.foodtruck.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.training.foodtruck.R
import com.training.foodtruck.adaptor.FaqAdaptor
import com.training.foodtruck.model.Faq
import kotlinx.android.synthetic.main.fragment_cart_success.*

/**
 * A simple [Fragment] subclass.
 */


//Frequently Asked Question fragment
class FaqFragment : Fragment() {

    lateinit var recyclerFaq:RecyclerView
    lateinit var faqAdaptor: FaqAdaptor
    lateinit var layoutmanager:RecyclerView.LayoutManager
    val faqList= arrayListOf<Faq>(
        Faq("Q1. Can I list up my favourite Restaurants ?","A.1 Yes,You can mark your favourites.They will saved in favourite Restaurants Tab "),
        Faq("Q2. How can i check my order History ?","A.2 Yes,When you plce an order,your history is instantly saved in order history tab"),
        Faq("Q3. Where can i check my profile ?","A.3 User profile is present in the user profile tab"),
        Faq("Q4. How to reset password ?","A.4 At the Login Page, a Forget Password? option is present below login button. After clicking it , you will asked to enter your registered mobile number and email. An Otp will be sent to your registered mail Id. You need to enter this Otp in this reset password screen. Along with it you will enter the new password.After changing password you will be redirected to the login page. Now you can login." )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_faq, container, false)
        recyclerFaq=view.findViewById(R.id.recyclerFaq)
        layoutmanager=LinearLayoutManager(activity as Context)
        faqAdaptor= FaqAdaptor(activity as Context,faqList)
        recyclerFaq.adapter=faqAdaptor
        recyclerFaq.layoutManager=layoutmanager

        return view
    }

}
