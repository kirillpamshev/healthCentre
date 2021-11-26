package ru.mgkit.healthcentre

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi

object DATA_KEYS {
    const val LOGIN_STRING = "LOGIN_STRING"
    const val PASSWORD_STRING = "PASSWORD_STRING"
}

class MainActivity : AppCompatActivity() {

    private var loginParameter = ""
    private var passwordParameter = ""
    private lateinit var loginValue: EditText
    private lateinit var passwordValue: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toLogin = findViewById<Button>(R.id.toGetIn)
        val toJoin = findViewById<Button>(R.id.toJoin)
        loginValue = findViewById<EditText>(R.id.LoginValue)
        passwordValue = findViewById<EditText>(R.id.PasswordValue)
        if (savedInstanceState != null) {
            loginValue.setText(savedInstanceState.getString(DATA_KEYS.LOGIN_STRING))
            passwordValue.setText(savedInstanceState.getString(DATA_KEYS.PASSWORD_STRING))
        }
        toLogin.setOnClickListener {
            if (isOnline(this)) {
                if (loginValue.text.isNotEmpty() && passwordValue.text.isNotEmpty()) {
                    loginParameter = loginValue.text.toString()
                    passwordParameter = passwordValue.text.toString()
                    startActivity(Intent(this, MenuOfButtons::class.java).apply { putExtra(DATA_KEYS.LOGIN_STRING, loginParameter) })
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
}
