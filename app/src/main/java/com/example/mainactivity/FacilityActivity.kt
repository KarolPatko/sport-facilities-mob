package com.example.mainactivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.mainactivity.ViewModel.FacilitiesFilterActivityViewModel
import com.example.mainactivity.ViewModel.FacilityActivityViewModel
import com.example.mainactivity.api.FacilityService
import com.example.mainactivity.api.UserService
import com.example.mainactivity.model.CommentDto
import com.example.mainactivity.model.MultipleFacilityItemDto
import com.example.mainactivity.model.PrefixUserItem
import com.example.mainactivity.model.SportDto
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

class FacilityActivity : AppCompatActivity(), CoroutineScope {
    private val job: Job = Job();
    override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + job

    companion object{
        var facilityIdd: Long = -1;
        lateinit var ctx: Context;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ctx = this.applicationContext
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facility)

        val facilityId = intent.getLongExtra("facilityId", -1);
        facilityIdd = facilityId;

        launch {
            getFacility(facilityId)
        }
    }

    private suspend fun getFacility(facilityId: Long) {
        val facilityResponse = FacilityActivityViewModel().getFacility(facilityId);

        if(facilityResponse != null){
            findViewById<TextView>(R.id.addressF).text = "Adres: "+facilityResponse.address.toString();
            findViewById<TextView>(R.id.districtF).text = "Dzielnica: "+facilityResponse.district.toString();
            findViewById<TextView>(R.id.sportsF).text = "Sporty: "+getSports(facilityResponse.sport);
            findViewById<TextView>(R.id.avgRate).text = "Srednia ocen: "+facilityResponse.rateAvg.toString();
            findViewById<TextView>(R.id.descriptionF).text = "Opis: "+facilityResponse.description.toString();
            makeList(facilityResponse.comment)
        }
        else{
            Toast.makeText(MainActivity.ctx, "Nie udało się pobrać obiektu", Toast.LENGTH_LONG).show()
        }
    }

    fun getSports(sports: Set<SportDto>?): String{
        var ret: String = "";
        if (sports != null) {
            for(s in sports){
                ret += s.name+", ";
            }
        }
        return ret.dropLast(2);
    }

    fun onAddComment(view: View){
        val intent = Intent(this, AddCommentActivity::class.java);
        intent.putExtra("facilityId", facilityIdd)
        startActivity(intent);
    }



    private fun makeList(content: Set<CommentDto>?){

        val listView = findViewById<ListView>(R.id.comments)
        val stringArrayList = ArrayList<CommentDto>()
        if (content != null) {
            for(el in content){
                stringArrayList.add(el)
            }
        }

        val stringArrayListNew = ArrayAdapter<CommentDto>(applicationContext, android.R.layout.simple_list_item_1, stringArrayList)
        listView.adapter = yourAdapter(this, stringArrayList)
        Thread.sleep(2000)
        stringArrayListNew.notifyDataSetChanged()
    }

    internal class yourAdapter(
        private var context: Context,
        private var data: ArrayList<CommentDto>
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
            if (vi == null) vi = inflater!!.inflate(R.layout.comment_item, null)

            val username = vi?.findViewById<View>(R.id.username) as TextView
            username.text = data[position].username
            val name = vi.findViewById<View>(R.id.name) as TextView
            name.text = data[position].content
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
}