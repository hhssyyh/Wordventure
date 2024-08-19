package com.example.wordventure.fairytale

import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.wordventure.R
import com.example.wordventure.pigepisode.Episode1

class PigActivity : AppCompatActivity() {

    private var openedEpi = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fairy_pig)

        openedEpi = intent.getIntExtra("opened_epi", 1)

        val epiBtn1: ImageButton = findViewById(R.id.epi_1)
        val epiBtn2: ImageButton = findViewById(R.id.epi_2)
        val epiBtn3: ImageButton = findViewById(R.id.epi_3)

        changeImageColor(epiBtn1, 1)
        changeImageColor(epiBtn2, 2)
        changeImageColor(epiBtn3, 3)

        epiBtn1.setOnClickListener {
            startEpisode(1)
        }
        epiBtn2.setOnClickListener {
            startEpisode(2)
        }
        epiBtn2.setOnClickListener {
            startEpisode(3)
        }
    }

    // 해금 안된 에피소드 흑백 처리
    private fun changeImageColor(button: ImageButton, buttonId: Int) {
        if (buttonId > openedEpi) {
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

    // 해금 여부 확인 후 해당 에피소드로
    private fun startEpisode(buttonId: Int) {
        if (buttonId > openedEpi) {
            showAlertDialog("먼저 이전 에피소드를 완료해주세요!")
        } else {
            try {
                // 'Episode' + buttonId로 클래스 이름 생성
                val className = "com.example.wordventure.pigepisode.Episode$buttonId"
                val episodeClass = Class.forName(className)

                val intent = Intent(this, episodeClass)
                startActivity(intent)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
                showAlertDialog("해당 에피소드를 찾을 수 없습니다.")
            }
        }
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