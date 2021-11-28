package ru.mgkit.healthcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MenuOfButtons : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_of_buttons)
        val toBackButton = findViewById<Button>(R.id.getOut)
        val aboutHealthCentre = findViewById<Button>(R.id.AboutHealthCentre)
        val toGetService = findViewById<Button>(R.id.getService)
        val getHistory = findViewById<Button>(R.id.getHistoryOfDisease)

        getHistory.setOnClickListener {
            startActivity(Intent(this, GetHistory::class.java) )
        }

        toBackButton.setOnClickListener {
            DATA_LOGIN.login = null
            startActivity(Intent(this, MainActivity::class.java))
        }

        aboutHealthCentre.setOnClickListener {
            startActivity(Intent(this, AboutHealthCentre::class.java))
        }

        toGetService.setOnClickListener {
            startActivity(Intent(this, GetService::class.java))
        }
    }


}