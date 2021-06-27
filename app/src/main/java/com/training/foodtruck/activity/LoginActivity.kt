package com.training.foodtruck.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.training.foodtruck.R
import com.training.foodtruck.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var etLoginMobile: EditText
    lateinit var etLoginPassword: EditText      //Variables declared
    lateinit var btnLogin: Button
    lateinit var txtForgetPassword: TextView
    lateinit var txtRegisterHere: TextView
    lateinit var sharedPreferences: SharedPreferences
    var mobileNumber:String?=null
    var password :String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)

        val isLoggedIn = sharedPreferences.getBoolean("IsLoggedIn", false)
        if (isLoggedIn) {
            val intent = Intent(this@LoginActivity, Showcase::class.java)
            startActivity(intent)
            finish()
        }
        setContentView(R.layout.activity_login)
        etLoginMobile = findViewById(R.id.etLoginMobile)
        etLoginPassword = findViewById(R.id.etLoginPassword)     //Variables initialized with their IDs
        btnLogin = findViewById(R.id.btnLogin)
        txtForgetPassword = findViewById(R.id.txtForgetPassword)
        txtForgetPassword.setOnClickListener {
            val intent=Intent(this,ForgetActivity::class.java)
            startActivity(intent)
        }
        txtRegisterHere = findViewById(R.id.txtRegisterHere)


        val intent = Intent(this@LoginActivity, Showcase::class.java)


        btnLogin.setOnClickListener()    //Adding Click Listener to Login Button
        {
            mobileNumber = etLoginMobile.text.toString()
            password = etLoginPassword.text.toString()
            if (mobileNumber == null && password == null) {
                Toast.makeText(this, "Some error have occurred", Toast.LENGTH_SHORT).show()
            }
            var queue = Volley.newRequestQueue(this@LoginActivity)

            val url = "http://13.235.250.119/v2/login/fetch_result"

            var jsonParams = JSONObject()

            jsonParams.put("mobile_number", mobileNumber)
            jsonParams.put("password", password)
            println("Login json sent are $jsonParams")

            if(ConnectionManager().checkConnectivity(this)) //Checking Internet Connection
            {

                var jsonObjectRequest = object : JsonObjectRequest(  //Json Request to send request
                    Request.Method.POST,
                    url,
                    jsonParams,
                    Response.Listener {
                        try {
                            println("Login Response is $it")
                            var jsonResponse = it.getJSONObject("data")
                            val success = jsonResponse.getBoolean("success")


                            if (success) {
                                val jsoninfo=jsonResponse.getJSONObject("data")
                                sharedPreferences.edit().putString("name",jsoninfo.getString("name")).apply()
                                sharedPreferences.edit().putString("user_id",jsoninfo.getString("user_id")).apply()
                                sharedPreferences.edit().putString("mobile_number",jsoninfo.getString("mobile_number")).apply()
                                sharedPreferences.edit().putString("address",jsoninfo.getString("address")).apply()
                                sharedPreferences.edit().putString("email",jsoninfo.getString("email")).apply()
                                savePreference()
                                startActivity(intent)
                                finish()
                            }



                            else {
                                Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show()
                            }

                        }
                        catch (e:JSONException)
                        {
                            Toast.makeText(this, "Some unexpected error occurred", Toast.LENGTH_SHORT).show()
                        }



                    },
                    Response.ErrorListener {
                        Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show()

                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers=HashMap<String,String>()
                        headers["Content-type"]="application/json"
                        headers["token"]="f88a3e996dc76f"
                        return headers
                    }


                }
                queue.add(jsonObjectRequest)

            }
            else
            {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Failed")
                dialog.setMessage("Internet Connection not found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()
                }
                dialog.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this)
                }
                dialog.create()
                dialog.show()
            }



        }


        this.txtForgetPassword.setOnClickListener() {   //Setting Click Listeners to Forget Password?
            val intent = Intent(this@LoginActivity, ForgetActivity::class.java)
            startActivity(intent)
            finish()
        }



        txtRegisterHere.setOnClickListener()   //Setting Click Listeners to Regsiter?
        {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun savePreference() {
        sharedPreferences.edit().putBoolean("IsLoggedIn", true).apply()   //Saving Boolean true in prefernece file to keep user logged in
    }
}
