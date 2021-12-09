package ru.mgkit.healthcentre

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseTheDoctor : AppCompatActivity() {
    private var doctorlist: List<TheDoctor> = listOf()
    private var adapter2 = AdapterDoctors()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_the_doctor)
        val spec = intent.getStringExtra(DATA_KEYS.SPEC_NAME)
        val context = this
        val recTable = findViewById<RecyclerView>(R.id.recycle_list_doctors)
        recTable.layoutManager = LinearLayoutManager(this)

        recTable.adapter = adapter2
        adapter2.setOnClickListener(object : AdapterDoctors.OnDoctorClickListener{
            override fun onDoctorClick(id_doctor: Int, doctor_name:String) {
                context.startActivity(Intent(context, EndOfGetService::class.java).apply { putExtra(DATA_KEYS.ID_DOCTOR, id_doctor)
                putExtra(DATA_KEYS.SPEC_NAME, spec)
                putExtra(DATA_KEYS.NAME_DOCTOR, doctor_name)})
            }
        })
        if (DATA_LOGIN.login != null){
            val call = RetrofitSingleton.service.getDoctors(spec!!)
            call.enqueue(object : Callback<List<TheDoctor>> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<List<TheDoctor>>, response: Response<List<TheDoctor>>) {
                    if (response.isSuccessful) {
                        val resp = response.body()
                        if (resp != null) {
                            doctorlist = resp
                            adapter2.setList(doctorlist)
                            adapter2.notifyDataSetChanged()
                        }
                    } else Toast.makeText(context, "Connection Error1", Toast.LENGTH_SHORT).show()
                }
                override fun onFailure(call: Call<List<TheDoctor>>, t: Throwable) {
                    Toast.makeText(context, "Connection Error: "+t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
        else Toast.makeText(this, "Empty login!", Toast.LENGTH_SHORT).show()
    }
}