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
import com.example.mainactivity.api.EventService
import com.example.mainactivity.model.ParticipantDto
import com.example.mainactivity.model.PrefixUserItem
import com.example.mainactivity.model.SingleEventItemDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DelParticipantsActivity : AppCompatActivity() {

    companion object{
        lateinit var ctx: Context;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_del_participants)
        makeList()
        ctx = applicationContext;
    }


    private fun makeList(){

        val listView = findViewById<ListView>(R.id.userspd)
        val stringArrayList = ArrayList<ParticipantDto>()
        if (ManageEventActivity.event.participants != null) {
            for(el in ManageEventActivity.event.participants){
                stringArrayList.add(el)
            }
        }

        val stringArrayListNew = ArrayAdapter<ParticipantDto>(applicationContext, android.R.layout.simple_list_item_1, stringArrayList)
        listView.adapter = yourAdapter(this, stringArrayList)
        Thread.sleep(2000)
        stringArrayListNew.notifyDataSetChanged()
    }

    internal class yourAdapter(
        private var context: Context,
        private var data: ArrayList<ParticipantDto>
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
            username.text = data[position].username
            val name = vi.findViewById<View>(R.id.name) as TextView
            name.text = data[position].name + " " + data[position].lastName

            val button = vi.findViewById(R.id.invite) as Button
            val inviteClickListener = InviteClickListener()
            inviteClickListener.userId = data[position].userId
            button.setOnClickListener(inviteClickListener)
            button.text = "Usuń"
            button.visibility = View.VISIBLE

            val button2 = vi.findViewById(R.id.buttonZ) as Button
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

    class InviteClickListener : View.OnClickListener, AppCompatActivity(){
        var userId: Long = 0
        override fun onClick(v: View?) {
            Log.d("ID", this.userId.toString())
            inviteFriend(userId)
        }

        private fun inviteFriend(userId: Long){
            val retrofit = Retrofit.Builder().baseUrl("http://192.168.1.171:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit
                .create(EventService::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                val response = service.delFromEvent(
                    ManageEventActivity.eventId,
                    userId
                )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Log.d("T", "udało się")
                    }
                    else{
                        Log.d("N", response.code().toString())
                    }
                }
            }
        }
    }
}