package com.example.wordventure.study

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.wordventure.R
import com.example.wordventure.RetrofitClient
import com.example.wordventure.WordData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudyWordDialog(context: Context, private val word: String, private val onWordSpoken: () -> Unit) : Dialog(context) {

    private lateinit var wordEng: TextView
    private lateinit var wordKr: TextView
    private lateinit var wordPro: TextView
    private lateinit var listenButton: ImageButton
    private lateinit var speakButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.study_word)

        val wordImg: ImageView = findViewById(R.id.word_img)

        wordEng = findViewById(R.id.word_eng_text)
        wordKr = findViewById(R.id.word_kr_text)
        wordPro = findViewById(R.id.word_pro_text)

        listenButton = findViewById(R.id.listenButton)
        speakButton = findViewById(R.id.speakButton)

        val resId = context.resources.getIdentifier("img_${word}", "drawable", context.packageName)
        if (resId != 0) {
            wordImg.setImageResource(resId)  // 리소스를 배경으로 설정
        } else {
            // 리소스를 찾을 수 없는 경우 기본값 설정
//            wordImg.setBackgroundResource(R.drawable.default_image)
        }

        // 서버로부터 데이터 요청
        requestWordData(word)

        // 발음 듣기 버튼 클릭 리스너
        listenButton.setOnClickListener {
            Toast.makeText(context, "$word 발음 듣기", Toast.LENGTH_SHORT).show()
        }

        // 말하기 버튼 클릭 리스너
        speakButton.setOnClickListener {
            onWordSpoken()
        }
    }

    private fun requestWordData(word: String) {
        val call = RetrofitClient.apiService.getWordData(word)

        call.enqueue(object : Callback<WordData> {
            override fun onResponse(call: Call<WordData>, response: Response<WordData>) {
                if (response.isSuccessful) {
                    // 서버에서 데이터를 성공적으로 받아온 경우
                    val responseData = response.body()
                    wordEng.text = word // 데이터 TextView에 표시
                    wordKr.text = responseData?.kr ?: "데이터 없음"
                    wordPro.text = responseData?.pron ?: ""
                } else {
                    Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WordData>, t: Throwable) {
            }
        })
    }
}