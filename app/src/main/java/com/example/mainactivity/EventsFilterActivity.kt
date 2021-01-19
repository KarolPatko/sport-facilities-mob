package com.example.mainactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.mainactivity.api.EventService
import com.example.mainactivity.api.FacilityService
import com.example.mainactivity.model.MultipleEventItemDto
import com.example.mainactivity.model.MultipleFacilityItemDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EventsFilterActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener{

    companion object{
        var events: List<MultipleEventItemDto> = emptyList();
        var districts: MutableSet<String> = mutableSetOf()
        var sports: MutableSet<String> = mutableSetOf();

        var chosenSportString: String = "";
        var chosenDistrictsString: String = "";
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_filter)
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
            val response = service.getEvents();

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    events = response.body()!!;
                    getFilterProp()
                }
                else{
                    Log.d("N", response.code().toString())
                }
            }
        }
    }

    fun getFilterProp(){
        for(el in events){
            districts.add(el.district)
            sports.add(el.sport)
        }

        var stringArrayList = ArrayList<String>()
        if (sports != null) {
            for(el in sports){
                stringArrayList.add(el)
            }
        }

        val spinnerSport: Spinner = findViewById(R.id.spinnerSportEvent);
        val myAdapter: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext,
                android.R.layout.simple_list_item_1,
                stringArrayList)
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSport.setAdapter(myAdapter)
        spinnerSport.onItemSelectedListener = this;

        stringArrayList = ArrayList<String>()
        if (districts != null) {
            for(el in districts){
                stringArrayList.add(el)
            }
        }
        val spinnerDistrict: Spinner = findViewById(R.id.spinnerDistrictEvent);
        val myAdapterDistrict: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext,
                android.R.layout.simple_list_item_1,
                stringArrayList)
        myAdapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDistrict.setAdapter(myAdapterDistrict)
        spinnerDistrict.onItemSelectedListener = this;
    }

    fun onFindEvents(view: View){
        val intent = Intent(this, EventsActivity::class.java);
        intent.putExtra("sport", chosenSportString)
        intent.putExtra("district", chosenDistrictsString)
        startActivity(intent);
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(parent?.id == R.id.spinnerSportEvent){
            val item: String = parent.getItemAtPosition(position).toString();
            val chosenSport = findViewById<TextView>(R.id.chosenSportEvent)
            chosenSport.text = item
            chosenSportString = item
        }
        if(parent?.id == R.id.spinnerDistrictEvent){
            val item: String = parent.getItemAtPosition(position).toString();
            val chosenSport = findViewById<TextView>(R.id.chosenDistrictEvent)
            chosenSport.text = item
            chosenDistrictsString = item
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}