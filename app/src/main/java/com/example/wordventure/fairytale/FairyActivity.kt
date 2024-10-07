package com.example.wordventure.fairytale

import android.content.Intent
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.wordventure.BaseActivity
import com.example.wordventure.R

class FairyActivity : BaseActivity() {

    private var openedEpi = 1
    private var fairyName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fairy)

        openedEpi = intent.getIntExtra("opened_epi", 1)
        fairyName = intent.getStringExtra("fairy_name") ?: ""
        val numberOfEpisodes = intent.getIntExtra("num_of_epi", 10)  // 총 에피소드 개수

        openedEpi = 1

        val buttonContainer: LinearLayout = findViewById(R.id.buttonContainer)
        val fairyImg: ImageView = findViewById(R.id.fairyImg)

        // 배경 이미지 설정
        val resId = resources.getIdentifier(fairyName, "drawable", packageName)
        fairyImg.setImageResource(resId)

        // 글자 넣어서 이미지 버튼 생성
        for (i in 1..numberOfEpisodes) {
            // FrameLayout을 생성해 ImageButton과 TextView를 겹치기
            val frameLayout = FrameLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(280.dp, 300.dp).apply {
                    setMargins(16.dp, 0, 16.dp, 0)  // 버튼 간격 설정 (마진 조정 가능)
                }
            }

            // ImageButton 생성
            val imageButton = ImageButton(this).apply {
                id = View.generateViewId()  // 각 버튼에 고유한 ID 부여
                setBackgroundResource(R.drawable.pig_epi_btn)  // 버튼의 배경 이미지 설정
                scaleType = ImageView.ScaleType.FIT_CENTER  // 이미지가 전체 크기에 맞게 조정되도록 설정
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                contentDescription = "Episode $i"

                setOnClickListener {
                    startEpisode(i)  // 클릭 시 startEpisode 함수 호출
                }

                // 만약 에피소드가 열리지 않은 상태라면 흑백 처리
                if (i > openedEpi) {
                    // Drawable을 복사하여 새로운 인스턴스를 사용
                    val drawable = this.background?.mutate()  // background를 mutate하여 복사본 생성
                    val matrix = ColorMatrix().apply { setSaturation(0f) }  // 흑백 필터 적용
                    val filter = ColorMatrixColorFilter(matrix)

                    drawable?.colorFilter = filter  // 흑백 필터 적용
                    this.background = drawable  // 필터 적용된 Drawable을 다시 배경으로 설정
                    isEnabled = false  // 버튼 비활성화
                }
            }

            // TextView 생성 (에피소드 번호 텍스트)
            val textView = TextView(this).apply {
                text = "Episode\n$i"
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)  // 텍스트 색상 설정
                textSize = 25f  // 텍스트 크기 설정
                typeface = Typeface.DEFAULT_BOLD  // 텍스트 스타일 설정
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER  // TextView를 위로 올리기 위해 topMargin 설정
                    topMargin = 35.dp
                }
            }

            // FrameLayout에 ImageButton과 TextView 추가
            frameLayout.addView(imageButton)
            frameLayout.addView(textView)

            // FrameLayout을 LinearLayout에 추가
            buttonContainer.addView(frameLayout)
        }
    }

    // 해금 여부 확인 후 해당 에피소드로
    private fun startEpisode(buttonId: Int) {
        if (buttonId > openedEpi) {
            showAlertDialog("먼저 이전 에피소드를 완료해주세요!")
        } else {
            try {
                // 'Episode' + buttonId로 클래스 이름 생성
                val intent = Intent(this, Episode::class.java)
                intent.putExtra("fairy_name", fairyName)
                intent.putExtra("epi_no", buttonId)
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

    // dp 값을 px로 변환하는 확장 함수
    val Int.dp: Int
        get() = (this * resources.displayMetrics.density).toInt()
}