package com.example.wordventure

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wordventure.R
import com.example.wordventure.RetrofitClient
import com.example.wordventure.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nameEditText: EditText = findViewById(R.id.nameEditText)
        val passwdEditText: EditText = findViewById(R.id.passwdEditText)
        val submitButton: Button = findViewById(R.id.submitButton)
        val fetchUsersButton: Button = findViewById(R.id.fetchUsersButton)

        submitButton.setOnClickListener {
            val id = nameEditText.text.toString()
            val passwd = passwdEditText.text.toString()
            val users = User(id, passwd)

            RetrofitClient.instance.addUsers(users).enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Users added successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("MainActivity", "Failed to add user: $errorBody")
                        Toast.makeText(this@MainActivity, "Failed to add users", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.e("MainActivity", "Request failed", t)
                    Toast.makeText(this@MainActivity, "Request failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}