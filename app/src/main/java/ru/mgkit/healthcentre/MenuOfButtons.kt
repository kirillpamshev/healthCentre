package ru.mgkit.healthcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MenuOfButtons : AppCompatActivity() {
    private var loginParameter = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_of_buttons)
        val toBackButton = findViewById<Button>(R.id.getOut)
        val aboutHealthCentre = findViewById<Button>(R.id.AboutHealthCentre)
        val toGetService = findViewById<Button>(R.id.getService)
        val getHistoryOfDisease = findViewById<Button>(R.id.getHistoryOfDisease)
        loginParameter = intent.getStringExtra(DATA_KEYS.LOGIN_STRING)!!
        if (savedInstanceState != null) {
            loginParameter = savedInstanceState.getString(DATA_KEYS.LOGIN_STRING, "")
        }
        getHistoryOfDisease.setOnClickListener {
            startActivity(Intent(this, GetHistory::class.java).apply { putExtra(DATA_KEYS.LOGIN_STRING, loginParameter) })
        }

        toBackButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        aboutHealthCentre.setOnClickListener {
            startActivity(Intent(this, AboutHealthCentre::class.java))
        }

        toGetService.setOnClickListener {
            startActivity(Intent(this, GetService::class.java).apply { putExtra(DATA_KEYS.LOGIN_STRING, loginParameter) })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(DATA_KEYS.LOGIN_STRING, loginParameter)
    }


}