package com.example.wordventure.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wordventure.BaseActivity
import com.example.wordventure.CardData
import com.example.wordventure.R
import com.example.wordventure.RetrofitClient
import com.example.wordventure.TokenManager
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordventure.CardAdapter

class MyIslandActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myisland)

        val collectedCardBtn: ImageButton = findViewById(R.id.collected_card)

        collectedCardBtn.setOnClickListener {
            showUserCardData()
        }

    }

    private fun showUserCardData() {
        val userId = TokenManager.getUserId(this)
        val call = RetrofitClient.apiService.getUserCards(userId)

        call.enqueue(object : Callback<List<CardData>> {
            override fun onResponse(call: Call<List<CardData>>, response: Response<List<CardData>>) {
                if (response.isSuccessful && response.body() != null) {
                    val cards = response.body()!!
                    showCardDialog(cards) // 팝업 창에 카드 데이터를 표시
                } else {
                    Toast.makeText(this@MyIslandActivity, "카드 데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CardData>>, t: Throwable) {
                Toast.makeText(this@MyIslandActivity, "서버와의 통신에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showCardDialog(cards: List<CardData>) {
        // 커스텀 다이얼로그 레이아웃을 인플레이트
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.myisland_card_display, null)

        // RecyclerView 설정
        val recyclerView: RecyclerView = dialogView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3) // 한 줄에 3개의 카드 배치
        recyclerView.adapter = CardAdapter(this, cards)

        // 다이얼로그 설정
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("닫기", null)
            .create()

        // 다이얼로그 표시
        dialog.show()
    }
}