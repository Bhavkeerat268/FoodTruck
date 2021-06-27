package com.training.foodtruck.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.training.foodtruck.R

class Splash : AppCompatActivity() {

    //THis is the the splash activity or the intro of the application

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        Handler().postDelayed(
                {
                    val startAct= Intent(this@Splash,
                        LoginActivity::class.java)
                    startActivity(startAct)
                    finish()
                },2000
        )
    }

}
