package com.example.mainactivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.mainactivity.api.EventService
import com.example.mainactivity.model.MultipleEventItemDto
import com.example.mainactivity.model.SingleEventItemDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ManageEventActivity : AppCompatActivity() {

    companion object{
        var eventId: Long = -1;
        lateinit var event: SingleEventItemDto;
        lateinit var ctx: Context;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_event)
        eventId = intent.getLongExtra("eventId", -1);
        ctx = applicationContext;
        get();
    }

    fun get(){
        val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.171:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        val service = retrofit
                .create(EventService::class.java);
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getEvent(eventId);

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    event = response.body()!!;
                    Log.d("a", event.dateEnd.toString())
                    Log.d("EVENTDDD", event.participants.size.toString())
                    findViewById<TextView>(R.id.addressDM).text = "Adres: "+ event.facility.address;
                    findViewById<TextView>(R.id.districtEM).text = "Dzielnica: "+ event.facility.disctrict;
                    findViewById<TextView>(R.id.dateStartEEM).text = "Data startu: "+ event.dateStart.replace("T", " ", true);
                    findViewById<TextView>(R.id.dateEndEEM).text = "Data zakończenia: "+ event.dateEnd.replace("T", " ", true);
                    findViewById<TextView>(R.id.sportEM).text = "Opis: "+ event.sport;
                    findViewById<TextView>(R.id.organizerEEM).text = "Organizator: "+ event.user.username;
                }
                else{
                    Log.d("N", response.code().toString())
                }
            }
        }
    }

    fun onDelete(view: View){
        val intent = Intent(this, DeleteEventConfirmaionActivity::class.java);
        intent.putExtra("eventId", eventId)
        startActivity(intent);
    }

    fun addParticipants(view: View){
        val intent = Intent(this, AddParticipantsActivity::class.java);
        startActivity(intent);
    }

    fun delParticipants(view: View){
        val intent = Intent(this, DelParticipantsActivity::class.java);
        startActivity(intent);
    }
}