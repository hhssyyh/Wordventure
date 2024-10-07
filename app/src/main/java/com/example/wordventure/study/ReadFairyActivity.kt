package com.example.wordventure.study

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.wordventure.BaseActivity
import com.example.wordventure.R
import com.example.wordventure.user.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ReadFairyActivity : BaseActivity() {

    private var fairyName = intent.getStringExtra("fairy_name") ?: ""
    private var epiNo = intent.getIntExtra("epi_no", 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 전달된 값으로 에피소드 레이아웃 매치
        val layoutName = "${fairyName}_episode${epiNo}"
        val layoutId = resources.getIdentifier(layoutName, "layout", packageName)

        if (layoutId != 0) {
            setContentView(layoutId)
        } else {
            setContentView(R.layout.pig_episode1)  // 기본 레이아웃
        }

        // 이미지 이름 패턴 및 시간을 동적으로 설정
        val imageCount = 2  // 페이지마다 다를 수 있음. 페이지마다 이미지 개수를 다르게 설정 가능
        val displayDurations = listOf(2000, 2000, 5000)  // 각 이미지의 표시 시간 (밀리초)

        showImagesDynamically(fairyName, imageCount, displayDurations)
    }

    // 이미지를 순차적으로 표시하는 함수
    private fun showImagesDynamically(fairyName: String, imageCount: Int, displayDurations: List<Int>) {
        val imageViews = mutableListOf<ImageView>()

        // 동적으로 ImageView를 가져옴
        for (i in 1..imageCount) {
            val resId = resources.getIdentifier("epi_read$i", "id", packageName)
            if (resId != 0) {  // 해당 ID가 존재하는 경우에만 추가
                val imageView = findViewById<ImageView>(resId)
                imageViews.add(imageView)
            }
        }

        // 동적으로 가져온 이미지뷰 리스트에 순차적으로 이미지를 적용
        CoroutineScope(Dispatchers.Main).launch {
            for (i in 1..imageViews.size) {
                val imageName = "${fairyName}_read$i"
                val imageId = resources.getIdentifier(imageName, "drawable", packageName)

                if (imageId != 0) {
                    // 해당 ImageView에 이미지 설정
                    val currentImageView = imageViews[i - 1]
                    currentImageView.setImageResource(imageId)

                    // 페이드 인 애니메이션 적용
                    val fadeInAnimation = AnimationUtils.loadAnimation(this@ReadFairyActivity, R.anim.fade_in)
                    currentImageView.startAnimation(fadeInAnimation)
                    currentImageView.visibility = View.VISIBLE

                    // 설정된 시간 동안 이미지 표시
                    delay(displayDurations[i - 1].toLong())

                    // 페이드 아웃 애니메이션 적용
                    val fadeOutAnimation = AnimationUtils.loadAnimation(this@ReadFairyActivity, R.anim.fade_out)
                    currentImageView.startAnimation(fadeOutAnimation)

                    delay(500)  // 잠시 대기

                    // 이미지 숨기기
                    currentImageView.visibility = View.GONE
                }
            }

            addNextButton()
        }
    }

    // 모든 이미지가 끝난 후 이미지 버튼 추가
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

            setOnClickListener {
                val intent = Intent(this@ReadFairyActivity, QuizActivity::class.java)
                intent.putExtra("fairy_name", fairyName)
                intent.putExtra("epi_no", epiNo)
                startActivity(intent)
                finish()  // 현재 Activity 종료
            }
        }

        // 동적으로 ImageButton을 추가
        parentLayout.addView(nextButton)
    }

    // dp 값을 px로 변환하는 확장 함수
    private val Int.dp: Int
        get() = (this * resources.displayMetrics.density).toInt()
}