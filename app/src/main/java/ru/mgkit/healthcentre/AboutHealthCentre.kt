package ru.mgkit.healthcentre

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AboutHealthCentre : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_helth_centre)

        val context = this.applicationContext
        val infoTableNameHC = findViewById<TextView>(R.id.info_table_nameHC)
        val infoTableAddressHC = findViewById<TextView>(R.id.info_table_addressHC)
        val infoTableContactMain = findViewById<TextView>(R.id.info_table_contactMain)
        val infoTableContactHot = findViewById<TextView>(R.id.info_table_contactHot)
        //infoTable.layoutManager = LinearLayoutManager(this)
        if (DATA_LOGIN.login != null){
            val call = RetrofitSingleton.service.getInfoAboutHC()
            call.enqueue(object : Callback<InfoHC> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<InfoHC>, response: Response<InfoHC>) {
                    if (response.isSuccessful) {
                        val resp = response.body()
                        if (resp != null) {
                            infoTableNameHC.text = resp.nameHC
                            infoTableAddressHC.text = resp.addressHC
                            infoTableContactMain.text = resp.contactMain
                            infoTableContactHot.text = resp.contactHot
                        }
                    } else Toast.makeText(context, "Connection Error1", Toast.LENGTH_SHORT).show()
                }
                override fun onFailure(call: Call<InfoHC>, t: Throwable) {
                    Toast.makeText(context, "Connection Error: "+t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
        else Toast.makeText(this, "Empty login!", Toast.LENGTH_SHORT).show()
    }


}
