package ru.mgkit.healthcentre

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


data class historyItems(var name:String, var spec: String, var datetime: String, var status: String)

class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>() {

    private var list : List<historyItems> = listOf()

    fun getList() = list


    fun setList(newList : List<historyItems>) {
        list = newList
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var name : TextView = itemView.findViewById(R.id.LFM_names_text)
        var spec : TextView = itemView.findViewById(R.id.spec_name_text)
        var datetime : TextView = itemView.findViewById(R.id.date_time_text)
        var status : TextView = itemView.findViewById(R.id.status_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_history_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text = list[position].name
        holder.spec.text = list[position].spec
        holder.datetime.text = list[position].datetime
        holder.status.text = list[position].status
        when(list[position].status) {
            "В проверке" -> holder.status.setTextColor(Color.YELLOW)
            "Закрыто" -> holder.status.setTextColor(Color.RED)
            "Проверено" -> holder.status.setTextColor(Color.GREEN)
            else -> holder.status.setTextColor(Color.GRAY)
        }
    }

    override fun getItemCount() = list.size


}

class GetHistory : AppCompatActivity() {
    private val adapter = Adapter()
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_history)

        val recTable = findViewById<RecyclerView>(R.id.recycleHistory)
        val testList: List<historyItems> = listOf(historyItems("Алексеев Андрей Михайлович","Хирург","2021.29.11","В проверке"),
            historyItems("Алексеев Андрей Михайлович","Хирург","2021.29.11","Закрыто"))
        recTable.adapter = adapter
        //adapter.setList(newList)
        //list = newList
        adapter.setList(testList)
        adapter.notifyDataSetChanged()
        Toast.makeText(this, DATA_LOGIN.login, Toast.LENGTH_SHORT ).show()
    }
}