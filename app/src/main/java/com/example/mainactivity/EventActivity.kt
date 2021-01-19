package com.example.mainactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.mainactivity.api.EventService
import com.example.mainactivity.api.FacilityService
import com.example.mainactivity.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EventActivity : AppCompatActivity() {
    companion object {
        lateinit var event: SingleEventItemDto;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)


        val eventId = intent.getLongExtra("eventId", -1);
        Log.d("EVENTID", eventId.toString());

        get(eventId)
    }

    private fun get(eventId: Long) {
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
                    Log.d("EVENT ONE", event?.toString())
                    findViewById<TextView>(R.id.addressD).text = "Adres: "+ event.facility.address;
                    findViewById<TextView>(R.id.districtE).text = "Dzielnica: "+event.facility.disctrict;
                    findViewById<TextView>(R.id.dateStartEE).text = "Data startu: "+event.dateStart.replace("T", " ", true);
                    findViewById<TextView>(R.id.dateEndEE).text = "Data zakończenia: "+event.dateEnd.replace("T", " ", true);
                    findViewById<TextView>(R.id.sportE).text = "Opis: "+event.sport;
                    findViewById<TextView>(R.id.organizerEE).text = "Organizator: "+event.user.username;
                }
                else{
                    Log.d("N", response.code().toString())
                }
            }
        }
    }
}