package com.example.wordventure.study

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wordventure.OpenedFairyResponse
import com.example.wordventure.R
import com.example.wordventure.RetrofitClient
import com.example.wordventure.TokenManager
import com.example.wordventure.User
import com.example.wordventure.user.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizActivity : AppCompatActivity() {

    private var fairyName = intent.getStringExtra("fairy_name") ?: ""
    private var fairyNo = intent.getIntExtra("fairy_no", 1)
    private var epiNo = intent.getIntExtra("epi_no", 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz)

        // 3개 단어 중 하나 골라서 이미지화
        getQuizWord{ quizWords ->

        }
    }

    // 해당 에피소드 단어들 가져오기
    private fun getQuizWord(callback: (List<String>?) -> Unit) {
        val call = RetrofitClient.apiService.getQuizWord(fairyNo, epiNo)

        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    Toast.makeText(this@QuizActivity, "Server error", Toast.LENGTH_SHORT).show()
                    callback(null)
                }
            }
            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Toast.makeText(this@QuizActivity, "Network error", Toast.LENGTH_SHORT).show()
                callback(null)
            }
        })
    }
}