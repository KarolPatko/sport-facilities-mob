package com.example.mainactivity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.example.mainactivity.ViewModel.CommentActivityViewModel
import com.example.mainactivity.ViewModel.FacilitiesFilterActivityViewModel
import com.example.mainactivity.api.CommentService
import com.example.mainactivity.api.FacilityService
import com.example.mainactivity.model.MultipleFacilityItemDto
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

class AddCommentActivity : AppCompatActivity(), CoroutineScope {
    private val job: Job = Job();
    override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + job

    companion object{
        var facilityId: Long = 0;
        lateinit var ctx: Context;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comment)
        facilityId = intent.getLongExtra("facilityId", -1);
        ctx = this.applicationContext
    }

    fun onAdd(view: View){
        launch{
            addComment();
        }
    }

    private suspend fun addComment(){
        val jsonObject = JSONObject()
        jsonObject.put("content", findViewById<EditText>(R.id.commentContent).text.toString());
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val commentResponse = CommentActivityViewModel().addComment(MainActivity.jwt, facilityId, requestBody);

        if(commentResponse in 200..299){
            Toast.makeText(ctx, "Dodałeś komentarz", Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(ctx, "Coś poszło nie tak", Toast.LENGTH_LONG).show()
        }
    }
}