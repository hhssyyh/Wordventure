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
    private var epiNo = intent.getIntExtra("epi_no", 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz)

        // 3개 단어 받아와서 그중 한개 그림으로(정답)

    }

    // 해당 에피소드 단어들 가져오기
//    private fun getQuizWord(callback: (Int?) -> Unit) {
//        val userId = TokenManager.getUserId(this)
//        val call = RetrofitClient.apiService.openedFairy(userId)
//
//        call.enqueue(object : Callback<OpenedFairyResponse> {
//            override fun onResponse(call: Call<OpenedFairyResponse>, response: Response<OpenedFairyResponse>) {
//                if (response.isSuccessful) {
//                    callback(response.body()?.fairyNo ?: 1)
//                } else {
//                    Toast.makeText(this@FairyIslandActivity, "Server error", Toast.LENGTH_SHORT).show()
//                    callback(null)
//                }
//            }
//            override fun onFailure(call: Call<OpenedFairyResponse>, t: Throwable) {
//                Toast.makeText(this@FairyIslandActivity, "Network error", Toast.LENGTH_SHORT).show()
//                callback(null)
//            }
//        })
//    }
}