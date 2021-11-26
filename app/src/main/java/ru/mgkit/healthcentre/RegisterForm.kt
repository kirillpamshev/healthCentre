package ru.mgkit.healthcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class RegisterForm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_form)
        val toJoin = findViewById<Button>(R.id.toJoin)
        val loginValue = findViewById<EditText>(R.id.LoginValue)
        val lastNameValue = findViewById<EditText>(R.id.LastName)
        val firstNameValue = findViewById<EditText>(R.id.FirstName)
        val passwordValue = findViewById<EditText>(R.id.PasswordValue)
            toJoin.setOnClickListener {
                if (loginValue.text.isNotEmpty() && lastNameValue.text.isNotEmpty() && firstNameValue.text.isNotEmpty() && passwordValue.text.isNotEmpty()) {
                    startActivity(Intent(this, MainActivity::class.java))
                    //todo TOAST -> ЗАРЕГИСТРИРОВАН!
                }
            }
    }
}