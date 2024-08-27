package com.example.wordventure.pig

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.wordventure.R

class Episode1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pig_episode1)

        val studyPigBtn: ImageButton = findViewById(R.id.study_pig)

        studyPigBtn.setOnClickListener {

        }
    }
}