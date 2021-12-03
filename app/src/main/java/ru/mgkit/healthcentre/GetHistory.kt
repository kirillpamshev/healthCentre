package ru.mgkit.healthcentre

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST



class GetHistory : AppCompatActivity() {
    private val adapter = Adapter()
    private var historylist: List<historyItems> = listOf()
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_history)
        val context = this.applicationContext
        val recTable = findViewById<RecyclerView>(R.id.recycleHistory)
        recTable.layoutManager = LinearLayoutManager(this)
        // val testList: List<historyItems> = listOf(historyItems("Алексеев Андрей Михайлович","Хирург","2021.29.11",0),
        //    historyItems("Алексеев Андрей Михайлович","Хирург","2021.29.11",1))
        recTable.adapter = adapter
        if (DATA_LOGIN.login != null){
            val call = RetrofitSingleton.service.getHistory(DATA_LOGIN.login!!)
            call.enqueue(object : Callback<List<historyItems>> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<List<historyItems>>, response: Response<List<historyItems>>) {
                    if (response.isSuccessful) {
                        val resp = response.body()
                        if (resp != null) {
                            historylist = resp
                            adapter.setList(historylist)
                            adapter.notifyDataSetChanged()
                        }
                    } else Toast.makeText(context, "Connection Error1", Toast.LENGTH_SHORT).show()
                }
                override fun onFailure(call: Call<List<historyItems>>, t: Throwable) {
                    Toast.makeText(context, "Connection Error: "+t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
        else Toast.makeText(this, "Empty login!", Toast.LENGTH_SHORT).show()
    }
}