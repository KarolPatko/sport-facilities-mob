package com.example.mainactivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.mainactivity.ViewModel.MainActivityViewModel
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private val job: Job = Job();
    override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ctx = this.applicationContext
    }

    companion object{
        var jwt: String = "";
        lateinit var ctx: Context;
    }

    fun onLogin(view: View) {
        val username = findViewById(R.id.username) as EditText;
        val password = findViewById(R.id.password) as EditText;
        launch {
            login(username.text.toString(), password.text.toString());
        }
    }

    fun onRegister(view: View) {
        val intent = Intent(this, RegisterActivity::class.java);
        startActivity(intent);
    }

    @SuppressLint("ShowToast")
    private suspend fun login(username: String, password: String) {
        val jsonObject = JSONObject()
        jsonObject.put("username", username)
        jsonObject.put("password", password)
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val tokens = MainActivityViewModel().getTokens(requestBody);

        if(tokens != null){
            jwt = tokens.jwt.toString();
            redirectMenu();
        }
        else{
            Toast.makeText(ctx, "Nie udane logowanie", Toast.LENGTH_LONG).show()
        }
    }

    private fun redirectMenu(){
        val intent = Intent(ctx, menuActivity::class.java);
        startActivity(intent);
    }
}