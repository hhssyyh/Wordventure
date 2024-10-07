package com.example.wordventure.study

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wordventure.OpenedFairyResponse
import com.example.wordventure.R
import com.example.wordventure.RetrofitClient
import com.example.wordventure.TokenManager
import com.example.wordventure.User
import com.example.wordventure.user.MainActivity
import com.example.wordventure.user.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizActivity : AppCompatActivity() {

    private var fairyName = ""
    private var fairyNo = 1
    private var epiNo = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz)

        fairyName = intent.getStringExtra("fairy_name") ?: ""
        fairyNo = intent.getIntExtra("fairy_no", 1)
        epiNo = intent.getIntExtra("epi_no", 1)

        // 3개 단어 중 하나 골라서 이미지화
        getQuizWord { quizWords ->
            quizWords?.let {
                // 3개의 단어 중 하나를 랜덤으로 선택
                val randomWord = it.random()

                // 단어를 기반으로 이미지 리소스 찾기
                val imageResId = resources.getIdentifier("img_$randomWord", "drawable", packageName)

                // 이미지뷰에 해당 이미지 설정
                val quizImageView = findViewById<ImageView>(R.id.quiz_img)
                quizImageView.setImageResource(imageResId)

                // 각 텍스트뷰에 단어 설정
                findViewById<TextView>(R.id.text1).text = it[0]
                findViewById<TextView>(R.id.text2).text = it[1]
                findViewById<TextView>(R.id.text3).text = it[2]

                // 랜덤으로 선택된 단어에 해당하는 버튼 클릭 리스너 설정
                when (randomWord) {
                    it[0] -> findViewById<ImageButton>(R.id.btn1).setOnClickListener {
                        goToNextActivity(randomWord)
                    }
                    it[1] -> findViewById<ImageButton>(R.id.btn2).setOnClickListener {
                        goToNextActivity(randomWord)
                    }
                    it[2] -> findViewById<ImageButton>(R.id.btn3).setOnClickListener {
                        goToNextActivity(randomWord)
                    }
                }
            } ?: run {
                // 에러 처리
                Toast.makeText(this, "Failed to load quiz words", Toast.LENGTH_SHORT).show()
            }
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

    // 다음 Activity로
    private fun goToNextActivity(selectedWord: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("selected_word", selectedWord)
        startActivity(intent)
    }
}