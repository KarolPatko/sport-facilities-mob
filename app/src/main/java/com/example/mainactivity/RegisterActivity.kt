package com.example.mainactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.mainactivity.api.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun onRegister(view: View){
        val username = findViewById(R.id.username) as EditText;
        var password = findViewById(R.id.password) as EditText;
        var name = findViewById(R.id.name) as EditText;
        var lastName = findViewById(R.id.lastName) as EditText;
        var email = findViewById(R.id.email) as EditText;

        register(username.text.toString(), password.text.toString(), name.text.toString(), lastName.text.toString(), email.text.toString());
    }

    fun register(username: String, password: String, name: String, lastName: String, email: String){

        if(!validate(username, password, name, lastName, email)){
            Log.d("V", "False");
            Toast.makeText(applicationContext, "Wpisz poprawne dane", Toast.LENGTH_LONG).show();
            return;
        }


        val jsonObject = JSONObject()
        jsonObject.put("username", username)
        jsonObject.put("password", password)
        jsonObject.put("name", name)
        jsonObject.put("lastName", lastName)
        jsonObject.put("email", email)
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.171:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        var service = retrofit
            .create(UserService::class.java);
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.register(requestBody);

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.d("TA", response.body()?.userId.toString())
                    Toast.makeText(applicationContext, "Poprawna rejestracja, przejdź do logowania", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(applicationContext, "Taki użytkownik już istnieje", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    fun validate(username: String, password: String, name: String, lastName: String, email: String): Boolean {
        if(username == null || username == "" || username.length < 4){
            return false;
        }

        val PASSWORD_PATTERN: String = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$"
        val pattern: Pattern = Pattern.compile(PASSWORD_PATTERN);
        if(!pattern.matcher(password).matches()){
            return false
        }

        val ONLY_LETTERS: String = "^[A-Za-z]+$";
        val pattern_letters: Pattern = Pattern.compile(ONLY_LETTERS);
        if(!pattern_letters.matcher(name).matches()){
            return false;
        }
        if(!pattern_letters.matcher(lastName).matches()){
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return false;
        }

        return true;
    }

}