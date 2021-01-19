package com.example.mainactivity

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.mainactivity.api.EventService
import com.example.mainactivity.api.FriendService
import com.example.mainactivity.api.UserService
import com.example.mainactivity.model.PrefixUserItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddParticipantsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_participants)
    }

    fun onFind(view: View) {
        val prefix = findViewById<EditText>(R.id.prefixp)
        get(prefix.text.toString())
    }

    private fun get(prefix: String) {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.171:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit
            .create(UserService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getUsersPrefix(MainActivity.jwt, prefix)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.d("TA", MainActivity.jwt+" "+ response.body()?.get(0)?.name.toString())
                    makeList(response.body())
                }
                else{
                    Log.d("N", response.code().toString())
                }
            }
        }
    }

    private fun makeList(content: Array<PrefixUserItem>?){

        val listView = findViewById<ListView>(R.id.usersp)
        val stringArrayList = ArrayList<PrefixUserItem>()
        if (content != null) {
            for(el in content){
                stringArrayList.add(el)
            }
        }

        val stringArrayListNew = ArrayAdapter<PrefixUserItem>(applicationContext, android.R.layout.simple_list_item_1, stringArrayList)
        listView.adapter = yourAdapter(this, stringArrayList)
        Thread.sleep(2000)
        stringArrayListNew.notifyDataSetChanged()
    }



    internal class yourAdapter(
        private var context: Context,
        private var data: ArrayList<PrefixUserItem>
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
            if(findFriendIndex(data[position].userId)){
                button.visibility = View.INVISIBLE
            }
            else{
                val inviteClickListener = InviteClickListener()
                inviteClickListener.userId = data[position].userId
                button.setOnClickListener(inviteClickListener)
                button.text = "Zaproś"
                button.visibility = View.VISIBLE
            }
            return vi
        }


        private fun findFriendIndex(userId: Long): Boolean{
            Log.d("A", userId.toString())
            for(el in ManageEventActivity.event.participants){
                Log.d("A", userId.toString()+" "+el.userId.toString())
                if(el.userId === userId){
                    Log.d("CCC.", el.userId.toString())
                    return true
                }
            }
            return false
        }

        companion object {
            private var inflater: LayoutInflater? = null
        }

        init {
            inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
    }

    class InviteClickListener : View.OnClickListener{
        var userId: Long = 0
        override fun onClick(v: View?) {
            Log.d("ID", this.userId.toString())
            inviteFriend(userId)
        }

        private fun inviteFriend(userId: Long){

            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.171:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit
                .create(EventService::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                val response = service.invite(MainActivity.jwt, ManageEventActivity.eventId, userId)

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