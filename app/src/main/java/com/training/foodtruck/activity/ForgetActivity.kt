package com.training.foodtruck.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.training.foodtruck.R
import com.training.foodtruck.fragments.ResetPasswordFragment
import com.training.foodtruck.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class ForgetActivity : AppCompatActivity() {
    lateinit var etForgetNumber: EditText
    lateinit var etForgetEmail: EditText            //Variables Declared
    lateinit var btnNext: Button
    lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget)
        etForgetNumber = findViewById(R.id.etForgetNumber)
        etForgetEmail = findViewById(R.id.etForgetEmail)      //Variables initialised
        frameLayout = findViewById(R.id.forgetFrame)
        btnNext = findViewById(R.id.btnNext)

        btnNext.setOnClickListener {   //Click listerner added to to button
            val mobileNumber = etForgetNumber.text.toString()
            val email = etForgetEmail.text.toString()


            val queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/forgot_password/fetch_result"
            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", mobileNumber)
            jsonParams.put("email", email)
            println("Json Params sent are $jsonParams")




            if (ConnectionManager().checkConnectivity(this)) {
                val jsonObjectRequest = object : JsonObjectRequest(
                    Method.POST,                                     //Json Prepared to send
                    url,
                    jsonParams,
                    Response.Listener {
                        println("Forget Response is $it")
                        val data = it.getJSONObject("data")
                        val result = data.getBoolean("success")
                        try {
                            if (result) {
                                val firstTry = data.getBoolean("first_try")
                                if (firstTry) {

                                    println("Otp Sent")
                                    val bundle = Bundle()
                                    bundle.putString("NumberMobile", mobileNumber)
                                    val frag = ResetPasswordFragment()
                                    frag.arguments = bundle
                                    supportFragmentManager.beginTransaction()    //Fragment replaced
                                        .replace(R.id.forgetFrame, frag)
                                        .commit()
                                } else {
                                    Toast.makeText(this, "OTP already sent", Toast.LENGTH_SHORT)
                                        .show()

                                    println("Otp Sent")
                                    val bundle = Bundle()
                                    bundle.putString("NumberMobile", mobileNumber)
                                    val frag = ResetPasswordFragment()
                                    frag.arguments = bundle
                                    supportFragmentManager.beginTransaction()
                                        .replace(R.id.forgetFrame, frag)
                                        .commit()
                                }
                            }

                        } catch (e: JSONException) {
                            Toast.makeText(
                                this,
                                "Some Unexpected error occurred",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(
                            this,
                            "Some Unexpected error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "f88a3e996dc76f"
                        return headers
                    }

                }
                queue.add(jsonObjectRequest)

            } else {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Error")
                dialog.setMessage("Internet connection not found")
                dialog.setPositiveButton("Turn On") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
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

    }
}
