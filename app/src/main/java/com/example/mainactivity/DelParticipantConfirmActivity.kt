package com.example.mainactivity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.mainactivity.api.EventService
import com.example.mainactivity.model.SingleEventItemDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DelParticipantConfirmActivity : AppCompatActivity() {
    companion object{
        var eventId: Long = -1;
        var userId: Long = -1;
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_del_participant_confirm)

        eventId = intent.getLongExtra("eventId", -1);
        userId = intent.getLongExtra("userId", -1);
    }

    fun onDelete(view: View){val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.171:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

        val service = retrofit
            .create(EventService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.delFromEvent( eventId, userId)

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