package com.example.wordventure.study

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.wordventure.BaseActivity
import com.example.wordventure.R

class ReadFairyActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fairyName = intent.getStringExtra("fairy_name") ?: ""
        val epiNo = intent.getIntExtra("epi_no", 1)

        // 전달된 값으로 에피소드 레이아웃 매치
        val layoutName = "${fairyName}_episode${epiNo}"
        val layoutId = resources.getIdentifier(layoutName, "layout", packageName)

        // 레이아웃 setContentView에 적용
        if (layoutId != 0) {
            setContentView(layoutId)
        } else {
            setContentView(R.layout.pig_episode1)  // 기본 레이아웃
        }

//        // pig_read1.png 이미지에 애니메이션 적용
//        val imageView: ImageView = findViewById(R.id.pigImageView)  // 레이아웃에 정의된 이미지뷰 ID로 대체
//        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
//        imageView.setImageResource(R.drawable.pig_read1)
//        imageView.startAnimation(fadeInAnimation)


    }
}