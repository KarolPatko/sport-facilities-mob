package com.example.mainactivity

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.mainactivity.api.EventService
import com.example.mainactivity.api.FacilityService
import com.example.mainactivity.api.FriendService
import com.example.mainactivity.model.NewEventDto
import com.example.mainactivity.model.SingleFacilityItemDto
import com.example.mainactivity.model.SportDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class CreateEventPart2Activity : AppCompatActivity(), AdapterView.OnItemSelectedListener{
    companion object{
        var sports: Set<SportDto> = emptySet<SportDto>();
        var facility: SingleFacilityItemDto? = null
        lateinit var chosenSportString: String
        var dateStart: String = ""
        var dateEnd: String = ""
        var timeStart: String = ""
        var timeEnd: String = ""
        lateinit var ctx: Context
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event_part2)

        val facilityId = intent.getLongExtra("facilityId", -1);
        get(facilityId);

        ctx = this.applicationContext
        val today = Calendar.getInstance()

        val datePickerStart = findViewById<DatePicker>(R.id.startEvent)
        datePickerStart.init(today.get(Calendar.YEAR),today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)){
            _, year, month, day ->
            run {
                val newMonth = month + 1;
                var writeMonth = newMonth.toString()
                var writeDay = day.toString()
                if(newMonth < 10) writeMonth = "0" + writeMonth;
                if(day < 10) writeDay = "0" + writeDay;
                dateStart = "$year-$writeMonth-$writeDay"
            }
        }

        val datePickerEnd = findViewById<DatePicker>(R.id.endEvent)
        datePickerEnd.init(today.get(Calendar.YEAR),today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)){
            _, year, month, day ->
            run {
                val newMonth = month + 1;
                var writeMonth = newMonth.toString()
                var writeDay = day.toString()
                if(newMonth < 10) writeMonth = "0" + writeMonth;
                if(day < 10) writeDay = "0" + writeDay;
                dateEnd = "$year-$writeMonth-$writeDay"
            }
        }

        val timePicker = findViewById<TimePicker>(R.id.startEventTime)
        timePicker.setOnTimeChangedListener { _, hour, minute -> var hour = hour
            var hourr = hour.toString()
            if(hour < 10){
                hourr = "0"+hour.toString()
            }
            var minutee = minute.toString()
            if(minute < 10){
                minutee = "0"+minute.toString()
            }
            timeStart = "$hourr:$minutee"
        }

        val timePickerend = findViewById<TimePicker>(R.id.endEventTime)
        timePickerend.setOnTimeChangedListener { _, hour, minute -> var hour = hour
            var hourr = hour.toString()
            if(hour < 10){
                hourr = "0"+hour.toString()
            }
            var minutee = minute.toString()
            if(minute < 10){
                minutee = "0"+minute.toString()
            }
            timeEnd = "$hourr:$minutee"
        }
    }




    private fun get(facilityId: Long) {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.171:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit
            .create(FacilityService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getFacility(facilityId)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.d("TABATA", response.body()?.address.toString())
                    facility = response.body()
                    getProps(response.body())
                }
                else{
                    Log.d("N", response.code().toString())
                }
            }
        }
    }

    fun getProps(facilityItemDto: SingleFacilityItemDto?){

        if (facilityItemDto != null) {
            sports = facilityItemDto.sport
        };

        var stringArrayList = ArrayList<String>()
        if (sports != null) {
            for(el in sports){
                stringArrayList.add(el.name)
            }
        }

        val spinnerSport: Spinner = findViewById(R.id.spinnerSportCreateEvent);
        val myAdapter: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext,
            android.R.layout.simple_list_item_1,
            stringArrayList)
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSport.setAdapter(myAdapter)
        spinnerSport.onItemSelectedListener = this;
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(parent?.id == R.id.spinnerSportCreateEvent){
            val item: String = parent.getItemAtPosition(position).toString();
            val chosenSport = findViewById<TextView>(R.id.chosenSportCreateEvent)
            chosenSport.text = item
            chosenSportString = item
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    fun onCreateEvent(view: View){
        val facilityId = facility?.facilityId.toString()
        val description = findViewById<EditText>(R.id.description).text.toString();
        Log.d("a", "$facilityId $chosenSportString $description $dateStart $dateEnd $timeStart $timeEnd")

        val jsonObject = JSONObject()
        jsonObject.put("facilityId", facilityId)
        jsonObject.put("dateStart", "$dateStart $timeStart")
        jsonObject.put("dateEnd", "$dateEnd $timeEnd")
        jsonObject.put("description", description)
        jsonObject.put("sportName", chosenSportString)
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.171:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit
                .create(EventService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.addEvent(MainActivity.jwt, requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Toast.makeText(ctx , "Dodano wydarzenie", Toast.LENGTH_SHORT).show()
                    Log.d("T", "udało się")
                }
                else{
                    if(response.code() == 409){
                        Toast.makeText(ctx , "Wydarzenie w takim czasie już istnieje.", Toast.LENGTH_SHORT).show()
                    }
                    else{

                        Toast.makeText(ctx , "Coś poszło nie tak.", Toast.LENGTH_SHORT).show()
                    }
                    Log.d("N", response.code().toString())
                }
            }
        }
    }

}