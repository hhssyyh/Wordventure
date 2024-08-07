package com.example.wordventure.user

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wordventure.R
import com.example.wordventure.RetrofitClient
import com.example.wordventure.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        val inputId: EditText = findViewById(R.id.id)
        val inputPasswd: EditText = findViewById(R.id.passwd)
        val checkIdBtn: Button = findViewById(R.id.chedkId)
        val registerBtn: Button = findViewById(R.id.register)
        val pwError: TextView = findViewById(R.id.pwError)

        inputId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkIdBtn.isEnabled = !s.isNullOrEmpty()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        checkIdBtn.setOnClickListener {
            val id = inputId.text.toString()
            checkIdAvailability(id)
        }

        registerBtn.setOnClickListener {
            val id = inputId.text.toString()
            val passwd = inputPasswd.text.toString()
            val user = User(id, passwd)
            if(id=="" || passwd=="") {
                Toast.makeText(this@RegisterActivity, "아이디와 비밀번호 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                addUser(user)
            }
        }

        inputPasswd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (!isValidPassword(password)) {
                    pwError.text = "비밀번호는 8자 이상이어야 하며, 숫자와 특수문자를 포함해야 합니다."
                    pwError.visibility = TextView.VISIBLE
                } else {
                    pwError.visibility = TextView.GONE
                }
            }
        })
    }

    private fun checkIdAvailability(id: String) {
        val call = RetrofitClient.apiService.checkId(id)

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    val available = response.body() ?: false
                    if (available) {
                        Toast.makeText(this@RegisterActivity, "ID is available", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@RegisterActivity, "ID is already taken", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "Server error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addUser(user: User) {
        val call = RetrofitClient.apiService.addUsers(user)

        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "User added successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RegisterActivity, "Failed to add user", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[!@#\$%^&*()_+=-]).{8,}\$"
        val pattern = Regex(passwordPattern)
        return pattern.matches(password)
    }
}