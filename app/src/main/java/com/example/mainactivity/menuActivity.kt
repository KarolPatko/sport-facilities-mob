package com.example.mainactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class menuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TOKEN", MainActivity.jwt)
        if(MainActivity.jwt.equals("")){
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent);
        }
        setContentView(R.layout.activity_menu)
    }

    override fun onResume() {
        super.onResume()
        Log.d("TOKEN", MainActivity.jwt)
        if(MainActivity.jwt.equals("")){
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent);
        }
    }

    fun onFriendRedirect(view: View){
        val intent = Intent(this, FriendsActivity::class.java);
        startActivity(intent);
    }

    fun onMyFriendRedirect(view: View){
        val intent = Intent(this, MyFriendsActivity::class.java);
        startActivity(intent);
    }

    fun onMyFriendInvitationsRedirect(view: View){
        val intent = Intent(this, MyFriendsInvitationsActivity::class.java);
        startActivity(intent);
    }

    fun onMyFacilitiesRedirect(view: View){
        val intent = Intent(this, FacilitiesFilterActivity::class.java);
        startActivity(intent);
    }

    fun onEventsRedirect(view: View){
        val intent = Intent(this, EventsFilterActivity::class.java);
        startActivity(intent);
    }

    fun onMyEventsRedirect(view: View){
        val intent = Intent(this, MyEventsActivity::class.java);
        startActivity(intent);
    }

    fun onCreateEvent(view: View){
        val intent = Intent(this, CreateEventActivity::class.java);
        startActivity(intent);
    }

    fun onLogout(view: View){
        val intent = Intent(this, MainActivity::class.java);
        MainActivity.jwt = "";
        startActivity(intent);
    }
}