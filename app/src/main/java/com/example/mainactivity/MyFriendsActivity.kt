package com.example.mainactivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mainactivity.api.FriendService
import com.example.mainactivity.model.FriendDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MyFriendsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_friends)
        get();
    }

    private fun get() {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.171:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        var service = retrofit
            .create(FriendService::class.java);
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getFriends(MainActivity.jwt);

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    makeList(response.body())
                }
                else{
                    Log.d("N", response.code().toString())
                }
            }
        }
    }

    fun makeList(content: List<FriendDto>?){

        val listView = findViewById<ListView>(R.id.users);
        var stringArrayList = ArrayList<FriendDto>()
        if (content != null) {
            for(el in content){
                stringArrayList.add(el)
            }
        }

        var stringArrayListNew = ArrayAdapter<FriendDto>(applicationContext, android.R.layout.simple_list_item_1, stringArrayList)
        listView.adapter = MyFriendsActivity.yourAdapter(this, stringArrayList);
        stringArrayListNew.notifyDataSetChanged()
    }

    internal class yourAdapter(
        var context: Context,
        var data: ArrayList<FriendDto>
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
            name.text = data[position].name + " " + data[position].lastName;

            val button = vi.findViewById<Button>(R.id.invite) as Button;
            val removeFriend: MyFriendsActivity.RemoveFriend =
                MyFriendsActivity.RemoveFriend();
            removeFriend.friendId = data[position].friendId;
            button.text = "Usuń";
            button.setOnClickListener(removeFriend)

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

    class RemoveFriend() : View.OnClickListener{
        public var friendId: Long = 0;

        override fun onClick(v: View?) {
            val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.171:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

            var service = retrofit
                .create(FriendService::class.java);
            CoroutineScope(Dispatchers.IO).launch {
                val response = service.deleteFriend(MainActivity.jwt, friendId);

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Log.d("N", response.code().toString())
                    }
                    else{
                        Log.d("N", response.code().toString())
                    }
                }
            }
            Log.d("ID", this.friendId.toString());
        }
    }
}