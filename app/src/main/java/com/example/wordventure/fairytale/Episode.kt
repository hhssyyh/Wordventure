package com.example.wordventure.fairytale

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.wordventure.BaseActivity
import com.example.wordventure.R
import com.example.wordventure.RetrofitClient
import com.example.wordventure.TokenManager
import com.example.wordventure.WordData
import com.example.wordventure.study.ReadFairyActivity
import com.example.wordventure.study.StudyWordDialog
import com.example.wordventure.user.MainActivity
import com.example.wordventure.user.MyIslandActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Episode : BaseActivity() {

    private var spokenCount = 0
    private val totalWords = 3
    private var fairyName = ""
    private var epiNo = 1
    private val spokenWords = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 이전 액티비티에서 전달된 인텐트 데이터 받음
        fairyName = intent.getStringExtra("fairy_name") ?: ""
        epiNo = intent.getIntExtra("epi_no", 1)

        // 전달된 값으로 에피소드 레이아웃 매치
        val layoutName = "${fairyName}_episode${epiNo}"
        val layoutId = resources.getIdentifier(layoutName, "layout", packageName)

        // 해당 레이아웃 setContentView에 적용
        if (layoutId != 0) {
            setContentView(layoutId)
        } else {
            // 해당 리소스가 없다면 기본 레이아웃 설정
            setContentView(R.layout.pig_episode1)  // 기본 레이아웃
        }

        val buttons = listOf<ImageButton>(
            findViewById(R.id.study_btn_1),
            findViewById(R.id.study_btn_2),
            findViewById(R.id.study_btn_3)
        )

        buttons.forEach { button ->
            // 버튼의 tag에서 단어를 가져옴
            val word = button.tag.toString()
            button.setOnClickListener {
                showStudyWordDialog(word)
            }
        }
    }

    private fun showStudyWordDialog(word: String) {
        val dialog = StudyWordDialog(this, word) {
            onWordSpoken(word)
        }
        dialog.show()
    }

    private fun onWordSpoken(word: String) {
        // spokenWords 리스트에 단어 추가
        if (!spokenWords.contains(word)) {
            spokenWords.add(word)
        }

        spokenCount++
        if (spokenCount == totalWords) {
            addNextButton() // 세 단어 모두 말했을 때만 다음 버튼 추가
        }
    }

    private fun addNextButton() {
        // ConstraintLayout을 찾음
        val parentLayout = findViewById<ConstraintLayout>(R.id.epi_layout)

        // ImageButton 동적 생성
        val nextButton = ImageButton(this).apply {
            setImageResource(R.drawable.epi_next)
            val sizeInDp = 100
            val scale = resources.displayMetrics.density
            val sizeInPx = (sizeInDp * scale).toInt() // dp 값 픽셀로 변환
            layoutParams = ConstraintLayout.LayoutParams(sizeInPx, sizeInPx)
            background = null
            scaleType = ImageView.ScaleType.CENTER_INSIDE
            layoutParams = ConstraintLayout.LayoutParams(sizeInPx, sizeInPx).apply {
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                setMargins(0, 0, 16.dp, 16.dp)  // 오른쪽 및 아래 여백 설정
            }

            id = View.generateViewId()

            setOnClickListener {
                // 학습한 단어 저장
                val userId = TokenManager.getUserId(this@Episode)
                val call = RetrofitClient.apiService.saveWords(userId, spokenWords)
                call.enqueue(object : Callback<Unit> {
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@Episode, "Words saved successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@Episode, "Failed to save words", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Toast.makeText(this@Episode, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })

                val intent = Intent(this@Episode, ReadFairyActivity::class.java)
                intent.putExtra("fairy_name", fairyName)
                intent.putExtra("epi_no", epiNo)
                startActivity(intent)
            }
        }

        // 동적으로 ImageButton을 추가
        parentLayout.addView(nextButton)
    }

    // dp 값을 px로 변환하는 확장 함수
    private val Int.dp: Int
        get() = (this * resources.displayMetrics.density).toInt()
}