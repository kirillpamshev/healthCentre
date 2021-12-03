package ru.mgkit.healthcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetService : AppCompatActivity() {
    lateinit var spinner: Spinner
    var spList: List<String> = listOf("")
    lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_service)
        val toChooseTheDocButton = findViewById<Button>(R.id.toChooseTheDoc)
        val activity = this
        spinner = findViewById(R.id.SpecSpinner)
        // val spList = arrayListOf<String>("Стоматолог", "Хирург")
        val call = RetrofitSingleton.service.getSpecs()
        call.enqueue(object : Callback<List<TheSpec>> {
            override fun onResponse(call: Call<List<TheSpec>>, response: Response<List<TheSpec>>) {
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null) {
                        if (resp.isNotEmpty()) {
                            spList = resp.map { it.spec_name!! }
                            adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, spList)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner.adapter = adapter
                        }
                        else Toast.makeText(activity, "Access Denied!", Toast.LENGTH_LONG).show()
                    } else Toast.makeText(activity, "Connection Error 1", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<List<TheSpec>>, t: Throwable) {
                Toast.makeText(activity, "Connection Error 2: " + t.message, Toast.LENGTH_LONG).show()
            }
        })

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

    fun getService() {
        val activity = this

    }

}