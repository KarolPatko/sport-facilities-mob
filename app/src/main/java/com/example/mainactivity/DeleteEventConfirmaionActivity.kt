package com.example.mainactivity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.mainactivity.api.EventService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DeleteEventConfirmaionActivity : AppCompatActivity() {
    companion object{
        lateinit var ctx: Context;
        var eventId: Long = -1;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_event_confirmaion)
        ctx = applicationContext;
        eventId = intent.getLongExtra("eventId", -1);
    }

    fun onDelete(view: View){
        val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.171:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        val service = retrofit
                .create(EventService::class.java);
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.deleteEvent(MainActivity.jwt, eventId);

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Toast.makeText(ctx, "Usunięto wydarzenie.", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(ctx, "Coś poszło nie tak.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}