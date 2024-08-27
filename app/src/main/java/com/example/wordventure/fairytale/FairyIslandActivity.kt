package com.example.wordventure.fairytale

import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.wordventure.MainActivity
import com.example.wordventure.OpenedEpiResponse
import com.example.wordventure.OpenedFairyResponse
import com.example.wordventure.R
import com.example.wordventure.RetrofitClient
import com.example.wordventure.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FairyIslandActivity : AppCompatActivity() {

    private var openedFairy = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fairy_island)

        val allIslandImg: ImageView = findViewById(R.id.allFairyIsland)
        val gotoMainBtn: ImageButton = findViewById(R.id.gotoMain)
        val fairyPigBtn: ImageButton = findViewById(R.id.fairy_pig)
        val fairyTurtleBtn: ImageButton = findViewById(R.id.fairy_turtle)

        // 서버에서 동화 해금 정보 불러오기
        getOpenedFairy { openedFairyFromServer ->
            if (openedFairyFromServer != null) {
                openedFairy = openedFairyFromServer
            }

            openedFairy = 1

            // 해금 정보에 따라 버튼 흑백 처리
            changeImageColor(fairyPigBtn, 1)
            changeImageColor(fairyTurtleBtn, 2)

            fairyPigBtn.setOnClickListener {
                startFairy("pig", 1)
            }
            fairyTurtleBtn.setOnClickListener {
                startFairy("turtle", 2)
            }
        }

        gotoMainBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // 동화 해금 정보 불러오기
    private fun getOpenedFairy(callback: (Int?) -> Unit) {
        val userId = TokenManager.getUserId(this)
        val call = RetrofitClient.apiService.openedFairy(userId)

        call.enqueue(object : Callback<OpenedFairyResponse> {
            override fun onResponse(call: Call<OpenedFairyResponse>, response: Response<OpenedFairyResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.fairyNo ?: 1)
                } else {
                    Toast.makeText(this@FairyIslandActivity, "Server error", Toast.LENGTH_SHORT).show()
                    callback(null)
                }
            }
            override fun onFailure(call: Call<OpenedFairyResponse>, t: Throwable) {
                Toast.makeText(this@FairyIslandActivity, "Network error", Toast.LENGTH_SHORT).show()
                callback(null)
            }
        })
    }

    // 해금 안된 동화 흑백 처리
    private fun changeImageColor(button: ImageButton, buttonId: Int) {
        if (buttonId > openedFairy) {
            // 배경 흑백 처리
            val background: Drawable? = button.background?.mutate() // mutate()를 사용하여 독립적인 Drawable로
            if (background != null) {
                val colorMatrix = ColorMatrix().apply { setSaturation(0f) }
                val colorFilter = ColorMatrixColorFilter(colorMatrix)
                background.colorFilter = colorFilter
            }
        } else {
            // 배경 컬러 복구
            button.background?.clearColorFilter()
        }
    }

    // 해금 여부 확인 후 해당 동화 페이지로
    private fun startFairy(fairyName: String, buttonId: Int) {
        if (buttonId > openedFairy) {
            showAlertDialog("먼저 이전 동화를 완료해주세요!")
        } else {
            try {
                getOpenedEpi(buttonId) { data ->
                    if (data != null) {
                        val className = "com.example.wordventure.fairytale.FairyActivity"
                        val episodeClass = Class.forName(className)

                        val intent = Intent(this, episodeClass)
                        intent.putExtra("fairy_name", fairyName)  // 동화명 전달
                        intent.putExtra("opened_epi", data.epiNo)  // 열린 에피소드 개수 전달
//                        intent.putExtra("num_of_epi", data.numOfEpi)  // 총 에피소드 개수 전달
                        intent.putExtra("num_of_epi", 10)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Failed to fetch episode number", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
                showAlertDialog("해당 동화를 찾을 수 없습니다.")
            }
        }
    }

    // 에피소드 해금 정보
    private fun getOpenedEpi(fairyNo: Int, callback: (OpenedEpiResponse?) -> Unit) {
        val userId = TokenManager.getUserId(this)
        val call = RetrofitClient.apiService.openedEpi(userId, fairyNo)

        call.enqueue(object : Callback<OpenedEpiResponse> {
            override fun onResponse(call: Call<OpenedEpiResponse>, response: Response<OpenedEpiResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    Log.d("FairyIslandActivity", "Response body: $response")
                    Toast.makeText(this@FairyIslandActivity, "Server error", Toast.LENGTH_SHORT).show()
                    callback(null)
                }
            }
            override fun onFailure(call: Call<OpenedEpiResponse>, t: Throwable) {
                Toast.makeText(this@FairyIslandActivity, "Network error", Toast.LENGTH_SHORT).show()
                callback(null)
            }
        })
    }

    // 알림창 띄우기
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