package com.example.wordventure.user

import android.content.Intent
import android.os.Bundle
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

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val inputId: EditText = findViewById(R.id.id)
        val inputPasswd: EditText = findViewById(R.id.passwd)
        val loginBtn = findViewById<Button>(R.id.login)
        val gotoRegisterButton = findViewById<Button>(R.id.gotoRegister)

        loginBtn.setOnClickListener {
            val id = inputId.toString()
            val passwd = inputPasswd.toString()
            val user = User(id, passwd)
            login(user)
        }

        gotoRegisterButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(user: User) {
        val call = RetrofitClient.apiService.login(user)

        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}