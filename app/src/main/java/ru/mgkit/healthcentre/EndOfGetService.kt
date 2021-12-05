package ru.mgkit.healthcentre

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text
import java.util.*
import android.widget.DatePicker

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener

import android.widget.TimePicker

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener

import android.text.format.DateUtils
import android.view.View


class EndOfGetService : AppCompatActivity() {
    lateinit var currentDateTime: TextView
    var dateAndTime: Calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_of_get_service)
        val specTextView = findViewById<TextView>(R.id.spec_name_text)
        val docNameTextView = findViewById<TextView>(R.id.nameDoc_text)
        val dateBut = findViewById<Button>(R.id.DateBut)
        val timeBut = findViewById<Button>(R.id.TimeBut)

        currentDateTime = findViewById<TextView>(R.id.DateTimeView)
        val butBegin = findViewById<Button>(R.id.begin_button)
        val doctor_id = intent.getIntExtra(DATA_KEYS.ID_DOCTOR,-1)
        val doctor_name = intent.getStringExtra(DATA_KEYS.NAME_DOCTOR)
        val doctor_spec = intent.getStringExtra(DATA_KEYS.SPEC_NAME)
        val login = DATA_LOGIN.login
        specTextView.text = doctor_spec
        docNameTextView.text = doctor_name

        butBegin.setOnClickListener {

        }

        dateBut.setOnClickListener {
            setDate(it)
        }

        timeBut.setOnClickListener {
            setTime(it)
        }
        setInitialDateTime()

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

    // отображаем диалоговое окно для выбора времени
    fun setTime(v: View?) {
        TimePickerDialog(
            this, t,
            dateAndTime[Calendar.HOUR_OF_DAY],
            dateAndTime[Calendar.MINUTE], true
        )
            .show()
    }

    // установка начальных даты и времени
    private fun setInitialDateTime() {
        currentDateTime.text = DateUtils.formatDateTime(
            this,
            dateAndTime.timeInMillis,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR
                    or DateUtils.FORMAT_SHOW_TIME
        )
    }

    // установка обработчика выбора времени
    var t =
        OnTimeSetListener { view, hourOfDay, minute ->
            dateAndTime[Calendar.HOUR_OF_DAY] = hourOfDay
            dateAndTime[Calendar.MINUTE] = minute
            setInitialDateTime()
        }

    // установка обработчика выбора даты
    var d =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            dateAndTime[Calendar.YEAR] = year
            dateAndTime[Calendar.MONTH] = monthOfYear
            dateAndTime[Calendar.DAY_OF_MONTH] = dayOfMonth
            setInitialDateTime()
        }
}