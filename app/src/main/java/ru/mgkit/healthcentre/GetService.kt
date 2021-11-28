package ru.mgkit.healthcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.GridLayout
import android.widget.Spinner

class GetService : AppCompatActivity() {
    lateinit var spinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_service)
        val toChooseTheDocButton = findViewById<Button>(R.id.toChooseTheDoc)
        spinner = findViewById(R.id.SpecSpinner)
        val spList = arrayListOf<String>("Стоматолог", "Хирург")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        if (savedInstanceState != null) {
            selectSpinnerItemByValue(spinner, savedInstanceState.getString(DATA_KEYS.SPEC_NAME))
        }
        toChooseTheDocButton.setOnClickListener {
            startActivity(Intent(this, ChooseTheDoctor::class.java).apply { putExtra(DATA_KEYS.SPEC_NAME, spinner.selectedItem as String) })
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(DATA_KEYS.SPEC_NAME, spinner.selectedItem as String)

    }

    fun selectSpinnerItemByValue(spnr: Spinner, value: String?) {
        if (value != null)
            for (position in 0 until spnr.count) {
                if (spnr.getItemAtPosition(position) === value) {
                    spnr.setSelection(position)
                    return
                }
            }
    }

}