package com.training.foodtruck.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.training.foodtruck.R
import com.training.foodtruck.activity.LoginActivity
import com.training.foodtruck.util.ConnectionManager
import org.json.JSONObject
import java.util.HashMap
import java.util.regex.Pattern

/**
 * A simple [Fragment] subclass.
 */
class ResetPasswordFragment : Fragment() {
    lateinit var etpassword: EditText
    lateinit var etotp: EditText
    lateinit var etConfirmPass: EditText
    lateinit var btnSubmit: Button
    lateinit var sharedPreferences:SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reset_password, container, false)
        etotp = view.findViewById(R.id.etOtp)
        etpassword = view.findViewById(R.id.etNewPassword)
        etConfirmPass = view.findViewById(R.id.etConfPassword)
        btnSubmit = view.findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            val otp = etotp.text.toString()
            val pass = etpassword.text.toString()
            val confirmPass = etConfirmPass.text.toString()
            val validatePassword = Pattern.compile(
                "^" +
                        "(?=.*[0-9])" +  //at least 1 digit
                        "(?=.*[a-z])" +  //at least 1 lower case letter
                        "(?=.*[A-Z])" +  //at least 1 upper case letter
                        "(?=.*[a-zA-Z])" +  //any letter
                        "(?=.*[@#$%^&+=])" +  //at least 1 special character
                        "(?=\\S+$)" +  //no white spaces
                        ".{4,}" +  //at least 4 characters
                        "$"
            ).matcher(pass).matches()
            when {
                otp.isEmpty() -> etotp.setError("This field cannot be empty")
                otp.length < 4 -> etotp.setError("Entered OTP must be 0f 4 characters")
                pass.isEmpty() -> etpassword.setError("This field cannot be empty")
                !validatePassword -> etpassword.setError(
                    "Password must contain at least 1 digit(0-9)," +
                            "1 lower case letter,at least 1 upper case letter,at least 1 special character,at least 4 characters"
                )
                !(pass == confirmPass) -> etConfirmPass.setError("Password do not match")
                else -> {
                    if (ConnectionManager().checkConnectivity(activity as Context)) {
                        val queue = Volley.newRequestQueue(activity as Context)
                        val url = "http://13.235.250.119/v2/reset_password/fetch_result"
                        val jsonParams = JSONObject()
                        val bundleMobile = this.arguments?.getString("NumberMobile")
                        jsonParams.put("mobile_number", bundleMobile)
                        jsonParams.put("password", pass)
                        jsonParams.put("otp", otp)
                        val jsonObjectRequest = object : JsonObjectRequest(
                            Method.POST,
                            url,
                            jsonParams,
                            Response.Listener {
                                val response=it.getJSONObject("data")
                                val result=response.getBoolean("success")
                                if(result)
                                {
                                    val toast=response.getString("successMessage")
                                    Toast.makeText(activity as Context,toast,Toast.LENGTH_SHORT).show()
                                    val intent=Intent(activity as Context,LoginActivity::class.java)
                                    startActivity(intent)
                                    sharedPreferences = activity!!.getSharedPreferences(getString(R.string.preference_file),Context.MODE_PRIVATE)
                                    sharedPreferences.edit().remove("name").apply()
                                    sharedPreferences.edit().remove("email").apply()
                                    sharedPreferences.edit().remove("password").apply()
                                    sharedPreferences.edit().remove("address").apply()
                                    sharedPreferences.edit().remove("mobile_number").apply()
                                    activity?.finishAffinity()
                                }
                            },
                            Response.ErrorListener { }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "f88a3e996dc76f"
                                return headers
                            }


                        }
                        queue.add(jsonObjectRequest)

                    }
                }


            }


        }
        return view
    }
}
