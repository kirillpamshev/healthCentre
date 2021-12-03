package ru.mgkit.healthcentre

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class MainActivity : AppCompatActivity() {

    private lateinit var loginValue: EditText
    private lateinit var passwordValue: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toLogin = findViewById<Button>(R.id.toGetIn)
        val toJoin = findViewById<Button>(R.id.toJoin)
        loginValue = findViewById(R.id.LoginValue)
        passwordValue = findViewById(R.id.PasswordValue)
        if (savedInstanceState != null) {
            loginValue.setText(savedInstanceState.getString(DATA_KEYS.LOGIN_STRING))
            passwordValue.setText(savedInstanceState.getString(DATA_KEYS.PASSWORD_STRING))
        }
        toLogin.setOnClickListener {
            if (isOnline(this)) {
                if (loginValue.text.isNotEmpty() && passwordValue.text.isNotEmpty()) {
                    autorization(loginValue.text.toString(), passwordValue.text.toString() )
                }
            } else {
                Toast.makeText(this,"Нет интернета!",Toast.LENGTH_SHORT).show()
            }
        }
        toJoin.setOnClickListener {
            if (isOnline(this)) {
                startActivity(Intent(this, RegisterForm::class.java))
            } else {
                Toast.makeText(this,"Нет интернета!",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
        return false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(DATA_KEYS.LOGIN_STRING, loginValue.text.toString())
        outState.putString(DATA_KEYS.PASSWORD_STRING, passwordValue.text.toString())
    }

    fun autorization(login: String, password: String) {
        val currentLoginInfo = LoginInfo(login, md5(password))
        val activity = this
        val call = RetrofitSingleton.service.getLogin(currentLoginInfo)
        call.enqueue(object : Callback<LoginAnswer> {
            override fun onResponse(call: Call<LoginAnswer>, response: Response<LoginAnswer>) {
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null) {
                        if (resp.isGranted) {
                            DATA_LOGIN.login = loginValue.text.toString()
                            startActivity(Intent(activity, MenuOfButtons::class.java) )
                        }
                        else Toast.makeText(activity, "Access Denied!", Toast.LENGTH_SHORT).show()
                    } else Toast.makeText(activity, "Connection Error", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<LoginAnswer>, t: Throwable) {
                Toast.makeText(activity, "Connection Error: " + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}

