package ru.mgkit.healthcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterForm : AppCompatActivity() {
    lateinit var loginValue:EditText
    lateinit var passwordValue: EditText
    lateinit var lastNameValue: EditText
    lateinit var firstNameValue: EditText
    lateinit var phoneValue: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_form)
        val toJoin = findViewById<Button>(R.id.toJoin)
        loginValue = findViewById(R.id.LoginValue)
        lastNameValue = findViewById(R.id.LastName)
        firstNameValue = findViewById(R.id.FirstName)
        passwordValue = findViewById(R.id.PasswordValue)
        phoneValue = findViewById(R.id.phoneKey)
        toJoin.setOnClickListener {
            if (loginValue.text.isNotEmpty() && lastNameValue.text.isNotEmpty() && firstNameValue.text.isNotEmpty() && passwordValue.text.isNotEmpty()) {
                if (checkPhone(phoneValue.text.toString())) {
                    createLogin(loginValue.text.toString(), passwordValue.text.toString(), firstNameValue.text.toString(), lastNameValue.text.toString(), phoneValue.text.toString())
                }
                else Toast.makeText(this, "Wrong phone number!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun createLogin(login: String, password: String, firstName: String, lastName: String, phoneNumber: String) {
        val currentRegisterInfoInfo = RegisterInfo(login, md5(password),firstName, lastName,phoneNumber)
        val activity = this
        val call = RetrofitSingleton.service.addUser(currentRegisterInfoInfo)
        call.enqueue(object : Callback<RegisterAnswer> {
            override fun onResponse(call: Call<RegisterAnswer>, response: Response<RegisterAnswer>) {
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null) {
                        if (resp.isOK) {
                            Toast.makeText(activity, "User was added successful!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else Toast.makeText(activity, resp.error, Toast.LENGTH_SHORT).show()
                    } else Toast.makeText(activity, "Connection Error", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<RegisterAnswer>, t: Throwable) {
                Toast.makeText(activity, "Connection Error: " + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
        fun checkPhone(phone: String): Boolean {
            return PhoneNumberUtils.isGlobalPhoneNumber(phone)
    }
}