package com.example.mainactivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.mainactivity.MainActivity.Companion.jwt
import com.example.mainactivity.api.EventService
import com.example.mainactivity.model.MultipleEventItemDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyEventsActivity : AppCompatActivity() {

    companion object{
        var events: List<MultipleEventItemDto> = emptyList();
        lateinit var ctx: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_events)
        val context = this.applicationContext
        ctx = context;
        get()
    }

    private fun get() {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.171:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        var service = retrofit
                .create(EventService::class.java);
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getMyEvents(jwt);

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    events = response.body()!!;
                    makeList()
                }
                else{
                    Log.d("N", response.code().toString())
                }
            }
        }
    }

    fun makeList(){

        val listView = findViewById<ListView>(R.id.my_events);
        var stringArrayList = ArrayList<MultipleEventItemDto>()
        if (events != null) {
            for(el in events){
                stringArrayList.add(el)
            }
        }

        var stringArrayListNew = ArrayAdapter<MultipleEventItemDto>(applicationContext, android.R.layout.simple_list_item_1, stringArrayList)
        listView.adapter = yourAdapter(this, stringArrayList);
        stringArrayListNew.notifyDataSetChanged()
    }

    internal class yourAdapter(
            var context: Context,
            var data: ArrayList<MultipleEventItemDto>
    ) :
            BaseAdapter() {
        override fun getCount(): Int {
            return data.size
        }

        override fun getItem(position: Int): Any {
            return data[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        @SuppressLint("SetTextI18n", "InflateParams")
        override fun getView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
        ): View {
            var vi = convertView
            if (vi == null) vi = inflater!!.inflate(R.layout.user_item, null)
            val username = vi?.findViewById<View>(R.id.username) as TextView
            username.text = data[position].username;
            val name = vi.findViewById<View>(R.id.name) as TextView
            name.text = data[position].address + ", " + data[position].sport + ", " + data[position].dateStart;

            val button = vi.findViewById<Button>(R.id.invite) as Button;
            val details: Details =
                    Details();
            details.eventId= data[position].eventId;
            button.text = "Szczegóły";
            button.setOnClickListener(details)

            val button2 = vi.findViewById<Button>(R.id.buttonZ) as Button;
            button2.visibility = View.INVISIBLE
            return vi
        }

        companion object {
            private var inflater: LayoutInflater? = null
        }

        init {
            inflater = context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
    }

    class Details() : View.OnClickListener{
        var eventId: Long = 0;

        override fun onClick(v: View?) {
            val intent = Intent(ctx, ManageEventActivity::class.java);
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            intent.putExtra("eventId", eventId)
            ContextCompat.startActivity(ctx, intent, null);
        }
    }
}