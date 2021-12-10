package ru.mgkit.healthcentre

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.math.BigInteger
import java.security.MessageDigest

object DATA_KEYS {
    const val NAME_DOCTOR = "NAME_DOCTOR"
    const val ID_DOCTOR = "ID_DOCTOR"
    const val SPEC_NAME = "SPEC_NAME"
    const val LOGIN_STRING = "LOGIN_STRING"
    const val PASSWORD_STRING = "PASSWORD_STRING"
    const val BASE_URL = "http://51.250.2.150:5000/"
}

object DATA_LOGIN {
    var login: String? = null
}

fun md5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}

data class LoginAnswer(
    @SerializedName("isGranted")
    @Expose
    var isGranted: Boolean = false
)

data class TheDoctor(
    @SerializedName("id_doctor")
    @Expose
    var id_doctor: Int? = null,
    @SerializedName("LFM_names")
    @Expose
    var LFM_names: String? = null
)

data class InfoHC(
    @SerializedName("nameHC")
    @Expose
    var nameHC: String? = null,
    @SerializedName("addressHC")
    @Expose
    var addressHC: String? = null,
    @SerializedName("contactMain")
    @Expose
    var contactMain: String? = null,
    @SerializedName("contactHot")
    @Expose
    var contactHot: String? = null,
)

data class TheSpec(
    @SerializedName("spec_name")
    @Expose
    var spec_name: String? = null
)

data class RegisterAnswer(
    @SerializedName("isOk")
    @Expose
    var isOK: Boolean = false,
    @SerializedName("error")
    @Expose
    var error: String = ""
)

data class LoginInfo(
    @SerializedName("login")
    @Expose
    var login: String? = null,
    @SerializedName("passwordHash")
    @Expose
    var passwordHash: String? = null
)

data class RegisterInfo(
    @SerializedName("login")
    @Expose
    var login: String? = null,
    @SerializedName("passwordHash")
    @Expose
    var passwordHash: String? = null,
    @SerializedName("firstname")
    @Expose
    var firstname: String? = null,
    @SerializedName("lastname")
    @Expose
    var lastname: String? = null,
    @SerializedName("phone")
    @Expose
    var phone: String? = null
)

data class historyItems(
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("spec")
    @Expose
    var spec: String? = null,
    @SerializedName("datetime")
    @Expose
    var datetime: String? = null,
    @SerializedName("status")
    @Expose
    var status: Int? = null
)


data class QueryTime(
    @SerializedName("id_doctor")
    @Expose
    var id_doctor: Int? = null,
    @SerializedName("date")
    @Expose
    var date: String? = null
)

data class FreeTime(
    @SerializedName("time")
    @Expose
    var time: String? = null
)

data class ServiceData(
    @SerializedName("codeDoctor")
    @Expose
    var codeDoctor: Int? = null,
    @SerializedName("login_bd")
    @Expose
    var login_bd: String? = null,
    @SerializedName("date")
    @Expose
    var date: String? = null,
    @SerializedName("time")
    @Expose
    var time: String? = null
)


interface ApiService {
    @POST("api/login")
    fun getLogin(@Body currentLoginInfo: LoginInfo): Call<LoginAnswer>

    @POST("api/register")
    fun addUser(@Body currentRegisterInfo: RegisterInfo): Call<RegisterAnswer>

    @POST("api/get_history")
    fun getHistory(@Body currentLogin: String): Call<List<historyItems>>

    @GET("api/get_specs")
    fun getSpecs(): Call<List<TheSpec>>

    @POST("api/get_doctors")
    fun getDoctors(@Body currentSpec: String): Call<List<TheDoctor>>

    @GET("api/get_info")
    fun getInfoAboutHC(): Call<InfoHC>

    @POST("api/get_time")
    fun getTime(@Body queryTime: QueryTime): Call<List<FreeTime>>

    @POST("api/get_service")
    fun addGetService(@Body serviceData: ServiceData): Call<RegisterAnswer>
}

object RetrofitSingleton {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(DATA_KEYS.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service: ApiService = retrofit.create(ApiService::class.java)
}


class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>() {
    private val statusArray: Array<String> = arrayOf("В проверке", "Закрыто", "Ожидается")

    private var list: List<historyItems> = listOf()

    fun getList() = list

    fun setList(newList: List<historyItems>) {
        list = newList
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.LFM_names_text)
        var spec: TextView = itemView.findViewById(R.id.spec_name_text)
        var datetime: TextView = itemView.findViewById(R.id.date_time_text)
        var status: TextView = itemView.findViewById(R.id.status_text)
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
        holder.status.text = statusArray.getOrElse(list[position].status!!) { statusArray[0] }
        when (list[position].status) {
            0 -> holder.status.setTextColor(Color.YELLOW)
            1 -> holder.status.setTextColor(Color.RED)
            2 -> holder.status.setTextColor(Color.GREEN)
            else -> holder.status.setTextColor(Color.GRAY)
        }
    }

    override fun getItemCount() = list.size

}


class AdapterDoctors : RecyclerView.Adapter<AdapterDoctors.HolderDoctor>() {

    private var list: List<TheDoctor> = listOf()

    interface OnDoctorClickListener {
        fun onDoctorClick(id_doctor: Int, doctor_name: String)
    }

    private var onClickListener: OnDoctorClickListener? = null

    fun setOnClickListener(onClickListener: OnDoctorClickListener) {
        this.onClickListener = onClickListener
    }

    fun getList() = list


    fun setList(newList: List<TheDoctor>) {
        list = newList
    }

    class HolderDoctor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.LFM_names_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDoctor {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_list_doctors_layout, parent, false)
        return HolderDoctor(view)
    }

    override fun onBindViewHolder(holder: HolderDoctor, position: Int) {
        holder.name.text = list[position].LFM_names
        holder.itemView.setOnClickListener {
            list[position].let { it1 ->
                it1.id_doctor?.let { it2 ->
                    it1.LFM_names?.let { it3 ->
                        onClickListener?.onDoctorClick(it2, it3)
                    }
                }
            }
            //onClickListener!!.onDoctorClick(list[position].id_doctor!!, list[position].LFM_names!!)
        }
    }

    override fun getItemCount() = list.size

}


class AdapterTime : RecyclerView.Adapter<AdapterTime.HolderTime>() {

    private var list: List<FreeTime> = listOf()

    interface OnTimeClickListener {
        fun onTimeClick(time: String)
    }

    private var onClickListener: OnTimeClickListener? = null

    fun setOnClickListener(onClickListener: OnTimeClickListener) {
        this.onClickListener = onClickListener
    }


    fun getList() = list


    fun setList(newList: List<FreeTime>) {
        list = newList
    }

    class HolderTime(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.LFM_names_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderTime {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_list_doctors_layout, parent, false)
        return HolderTime(view)
    }

    override fun onBindViewHolder(holder: HolderTime, position: Int) {
        holder.name.text = list[position].time
        holder.itemView.setOnClickListener {
            list[position].let { it1 ->
                it1.time?.let { it2 ->
                    onClickListener?.onTimeClick(
                        it2
                    )
                }
            }
        }
    }

    override fun getItemCount() = list.size

}
