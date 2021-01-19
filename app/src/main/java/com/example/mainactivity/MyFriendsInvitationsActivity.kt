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
import com.example.mainactivity.api.FriendService
import com.example.mainactivity.model.FriendInvitation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyFriendsInvitationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_friends_invitations)
        get();
    }

    private fun get(){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.171:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit
            .create(FriendService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getInvitations(MainActivity.jwt)

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

    private fun makeList(content: List<FriendInvitation>?){

        val listView = findViewById<ListView>(R.id.users);
        val stringArrayList = ArrayList<FriendInvitation>()
        if (content != null) {
            for(el in content){
                stringArrayList.add(el)
            }
        }

        Log.d("DDDDD", content?.size.toString())
        val stringArrayListNew = ArrayAdapter<FriendInvitation>(applicationContext, android.R.layout.simple_list_item_1, stringArrayList)
        listView.adapter = MyFriendsInvitationsActivity.yourAdapter(this, stringArrayList);
        stringArrayListNew.notifyDataSetChanged()
    }

    internal class yourAdapter(
        var context: Context,
        var data: ArrayList<FriendInvitation>
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
            if(data[position].inviter === true){
                button.text = "Cofnij";
            }
            else{
                button.text = "Odrzuć";
            }
            val removeFriend = RemoveFriend();
            removeFriend.friendId = data[position].friendId;
            button.setOnClickListener(removeFriend)

            val button2 = vi.findViewById<Button>(R.id.buttonZ) as Button;
            if(data[position].inviter === false){
                button2.text = "Akceptuj";
                val acceptFriend = AcceptFriend();
                acceptFriend.friendId = data[position].friendId;
                button2.setOnClickListener(acceptFriend)
            }
            else{
                button2.visibility = View.INVISIBLE
            }

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



    class AcceptFriend() : View.OnClickListener{
        public var friendId: Long = 0;

        override fun onClick(v: View?) {
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.1.171:8080")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            var service = retrofit
                    .create(FriendService::class.java);
            CoroutineScope(Dispatchers.IO).launch {
                val response = service.acceptFriend(MainActivity.jwt, friendId);

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