package ru.mgkit.healthcentre

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.*
import java.text.SimpleDateFormat
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener

import android.text.format.DateUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class EndOfGetService : AppCompatActivity() {
    private var timelist: List<FreeTime> = listOf()
    lateinit var currentDate: TextView
    lateinit var recycle_time: RecyclerView
    var  doctor_id by Delegates.notNull<Int>()
    val adapter3 = AdapterTime()
    var dateAndTime: Calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_of_get_service)
        val specTextView = findViewById<TextView>(R.id.spec_name_text)
        val docNameTextView = findViewById<TextView>(R.id.nameDoc_text)
        val dateBut = findViewById<Button>(R.id.DateBut)
        currentDate = findViewById<TextView>(R.id.DateView)
        doctor_id = intent.getIntExtra(DATA_KEYS.ID_DOCTOR,-1)
        val doctor_name = intent.getStringExtra(DATA_KEYS.NAME_DOCTOR)
        val doctor_spec = intent.getStringExtra(DATA_KEYS.SPEC_NAME)
        val login = DATA_LOGIN.login
        specTextView.text = doctor_spec
        docNameTextView.text = doctor_name
        dateBut.setOnClickListener {
            setDate(it)
        }
        setInitialDateTime()
        recycle_time = findViewById<RecyclerView>(R.id.recycle_time)
        recycle_time.layoutManager = LinearLayoutManager(this)
        recycle_time.adapter = adapter3
        val activity = this
        adapter3.setOnClickListener(object : AdapterTime.OnTimeClickListener{
            override fun onTimeClick(time: String) {
                val call = RetrofitSingleton.service.addGetService(ServiceData(doctor_id,
                    DATA_LOGIN.login, currentDate.text.toString(), time))
                call.enqueue(object : Callback<RegisterAnswer> {
                    override fun onResponse(call: Call<RegisterAnswer>, response: Response<RegisterAnswer>) {
                        if (response.isSuccessful) {
                            val resp = response.body()
                            if (resp != null) {
                                if (resp.isOK) {
                                    Toast.makeText(activity, "The registration was added successful!", Toast.LENGTH_SHORT).show()
                                    activity.finish()
                                } else Toast.makeText(activity, resp.error, Toast.LENGTH_SHORT).show()
                            } else Toast.makeText(activity, "Connection Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<RegisterAnswer>, t: Throwable) {
                        Toast.makeText(activity, "Connection Error: " + t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        })
    }

    // отображаем диалоговое окно для выбора даты
    fun setDate(v: View?) {
        DatePickerDialog(
            this, d,
            dateAndTime[Calendar.YEAR],
            dateAndTime[Calendar.MONTH],
            dateAndTime[Calendar.DAY_OF_MONTH]
        )
            .show()
    }

    // установка начальных даты и времени
    private fun setInitialDateTime() {
        val date = Date(dateAndTime.timeInMillis)
        currentDate.text = SimpleDateFormat("dd.MM.yyyy").format(date)
    }

    // установка обработчика выбора даты
    var d =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            dateAndTime[Calendar.YEAR] = year
            dateAndTime[Calendar.MONTH] = monthOfYear
            dateAndTime[Calendar.DAY_OF_MONTH] = dayOfMonth
            setInitialDateTime()
            freetimesquery()
        }

    private fun freetimesquery() {
        val context = this
        val call = RetrofitSingleton.service.getTime(QueryTime(doctor_id, currentDate.text.toString()))
        call.enqueue(object : Callback<List<FreeTime>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<List<FreeTime>>, response: Response<List<FreeTime>>) {
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null) {
                        if (resp.isEmpty()) {
                            Toast.makeText(context,"No available to book time", Toast.LENGTH_SHORT).show()
                        }
                        timelist = resp
                        adapter3.setList(timelist)
                        adapter3.notifyDataSetChanged()
                    }
                } else Toast.makeText(context, "Connection Error1", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<List<FreeTime>>, t: Throwable) {
                Toast.makeText(context, "Connection Error: "+t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

}