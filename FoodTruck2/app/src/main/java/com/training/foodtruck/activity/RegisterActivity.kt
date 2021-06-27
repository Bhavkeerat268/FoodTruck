package com.training.foodtruck.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.training.foodtruck.util.ConnectionManager
import com.training.foodtruck.R
import org.json.JSONObject
import java.util.HashMap
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    lateinit var etRegisterName: EditText
    lateinit var etRegisterEmail: EditText
    lateinit var etRegisterMobileNumber: EditText
    lateinit var etRegisterDeliveryAddress: EditText
    lateinit var etRegisterPassword: EditText
    lateinit var btnRegisterAccount: Button
    lateinit var etRegisterConfirmpassword: EditText
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etRegisterName = findViewById(R.id.etRegisterName)
        etRegisterEmail = findViewById(R.id.etRegisterEmail)
        etRegisterMobileNumber = findViewById(R.id.etRegisterMobileNumber)
        etRegisterDeliveryAddress = findViewById(R.id.etRegiterDeliveryAddress)
        etRegisterPassword = findViewById(R.id.etRegiterPassword)
        etRegisterConfirmpassword = findViewById(R.id.etRegisterConfirmPassword)
        btnRegisterAccount = findViewById(R.id.btnRegisterAccount)
        btnRegisterAccount.setOnClickListener {
            val registerName = etRegisterName.text.toString()
            val registerEmail = etRegisterEmail.text.toString()
            val registerMobileNumber = etRegisterMobileNumber.text.toString()
            val registerDeliveryAddress = etRegisterDeliveryAddress.text.toString()
            val registerPassword = etRegisterPassword.text.toString()
            val registerConfirmPassword = etRegisterConfirmpassword.text.toString()

            val validateEmail = Patterns.EMAIL_ADDRESS.matcher(registerEmail).matches()
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
            ).matcher(registerPassword).matches()
            when {
                registerName.isEmpty() -> etRegisterName.setError("Name cannot be empty!")
                (registerName.length) < 3 -> etRegisterName.setError("Name is too short,Enter atleast 3 characters!")
                !validateEmail -> etRegisterEmail.setError("Entered email address is not valid!")
                registerEmail.isEmpty() -> etRegisterEmail.setError("Email address field cannot be empty")
                registerMobileNumber.isEmpty() -> etRegisterMobileNumber.setError("Mobile Number can't be empty")
                registerMobileNumber.length < 10 -> etRegisterMobileNumber.setError("Mobile number must be of 10 digits")
                registerDeliveryAddress.isEmpty() -> etRegisterDeliveryAddress.setError("You must enter Delivery Address")
                registerPassword.isEmpty() -> etRegisterPassword.setError("Password can't be empty")
                !validatePassword -> etRegisterPassword.setError(
                    "Password must contain at least 1 digit(0-9)," +
                            "1 lower case letter,at least 1 upper case letter,at least 1 special character,at least 4 characters"
                )
                !(registerPassword == registerConfirmPassword) -> etRegisterConfirmpassword.setError(
                    "Passwords do not match"
                )
                else -> {   if (ConnectionManager().checkConnectivity(this@RegisterActivity)) {
                    val intent = Intent(this@RegisterActivity, Showcase::class.java)
                    sharedPreferences = getSharedPreferences(
                        getString(R.string.preference_file),
                        Context.MODE_PRIVATE)
                    sharedPreferences.edit().putString("name", registerName)
                        .apply()
                    sharedPreferences.edit()
                        .putString("email", registerEmail).apply()
                    sharedPreferences.edit()
                        .putString("password", registerPassword).apply()
                    sharedPreferences.edit().putString(
                        "address",
                        registerDeliveryAddress
                    ).apply()
                    sharedPreferences.edit().putBoolean("IsLoggedIn",true).apply()
                    sharedPreferences.edit()
                        .putString("mobile_number", registerMobileNumber)
                        .apply()

                    val queue = Volley.newRequestQueue(this@RegisterActivity)
                    val url = "http://13.235.250.119/v2/register/fetch_result"
                    val jsonParams = JSONObject()
                    jsonParams.put("name", registerName)
                    jsonParams.put("mobile_number", registerMobileNumber)
                    jsonParams.put("password", registerPassword)
                    jsonParams.put("address", registerDeliveryAddress)
                    jsonParams.put("email", registerEmail)
                    val jSonObjectRequest = object : JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        jsonParams,
                        Response.Listener {
                            val jsonResponse = it.getJSONObject("data")
                            val success = jsonResponse.getBoolean("success")
                            if (success) {
                                println("Response is $it")
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Registered",
                                    Toast.LENGTH_LONG
                                ).show()



                            }

                        },
                        Response.ErrorListener {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Some unexpected error has occurred",
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
                    queue.add(jSonObjectRequest)
                    startActivity(intent)
                    finish()

                }




                }


            }


        }
    }
}
