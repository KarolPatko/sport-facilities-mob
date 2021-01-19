package com.example.mainactivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.mainactivity.ViewModel.FacilitiesFilterActivityViewModel
import com.example.mainactivity.ViewModel.MainActivityViewModel
import com.example.mainactivity.api.FacilityService
import com.example.mainactivity.api.FriendService
import com.example.mainactivity.model.FriendDto
import com.example.mainactivity.model.MultipleFacilityItemDto
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

class FacilitiesFilterActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, CoroutineScope{
    private val job: Job = Job();
    override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + job

    companion object{
        var facilities: List<MultipleFacilityItemDto> = emptyList();
        var districts: MutableSet<String> = mutableSetOf()
        var sports: MutableSet<String> = mutableSetOf();

        var address: String = "";
        var chosenSportString: String = "";
        var chosenDistrictsString: String = "";
        lateinit var ctx: Context;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facilities_filter)
        ctx = this.applicationContext
        launch {
            getFacilities();
        }
    }

    private suspend fun getFacilities() {
        val facilitiesResponse = FacilitiesFilterActivityViewModel().getFacilities();

        if(facilitiesResponse != null){
            facilities = facilitiesResponse;
            getFilterProp()
        }
        else{
            Toast.makeText(MainActivity.ctx, "Nie udało się pobrać obiektów", Toast.LENGTH_LONG).show()
        }
    }

    fun getFilterProp(){
        for(el in facilities){
            for(sport in el.sport){
                sports.add(sport.name)
                Log.d("KI", sport.name)
            }
            districts.add(el.district)
        }

        var stringArrayList = ArrayList<String>()
        if (sports != null) {
            for(el in sports){
                stringArrayList.add(el)
            }
        }

        val spinnerSport: Spinner = findViewById(R.id.spinnerSport);
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
        var spinnerDistrict: Spinner = findViewById(R.id.spinnerDistrict);
        var myAdapterDistrict: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext,
            android.R.layout.simple_list_item_1,
            stringArrayList)
        myAdapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDistrict.setAdapter(myAdapterDistrict)
        spinnerDistrict.onItemSelectedListener = this;
    }

    fun onFindFacilities(view: View){
        val intent = Intent(this, Facilities::class.java);
        val address = findViewById<EditText>(R.id.addressFF);
        intent.putExtra("sport", chosenSportString)
        intent.putExtra("district", chosenDistrictsString)
        intent.putExtra("address", address.text.toString())
        startActivity(intent);
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(parent?.id == R.id.spinnerSport){
            val item: String = parent.getItemAtPosition(position).toString();
            val chosenSport = findViewById<TextView>(R.id.chosenSport)
            chosenSport.text = item
            chosenSportString = item
        }
        if(parent?.id == R.id.spinnerDistrict){
            val item: String = parent.getItemAtPosition(position).toString();
            val chosenSport = findViewById<TextView>(R.id.chosenDistrict)
            chosenSport.text = item
            chosenDistrictsString = item
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}