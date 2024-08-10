package com.example.wordventure.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.wordventure.LoginResponse
import com.example.wordventure.MainActivity
import com.example.wordventure.R
import com.example.wordventure.RetrofitClient
import com.example.wordventure.TokenManager
import com.example.wordventure.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var token: String

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

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    token = loginResponse?.token ?: ""
                    // 토큰 저장
                    TokenManager.saveToken(this@LoginActivity, token)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                } else {
                    showAlertDialog("아이디나 비밀번호가 잘못되었습니다.")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showAlertDialog(msg: String) {
        AlertDialog.Builder(this).apply {
            setTitle("알림")
            setMessage(msg)
            setPositiveButton("확인", null)
            create()
            show()
        }
    }
}