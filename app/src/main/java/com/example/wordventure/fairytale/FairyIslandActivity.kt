package com.example.wordventure.fairytale

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.wordventure.MainActivity
import com.example.wordventure.R
import com.example.wordventure.user.LoginActivity

class FairyIslandActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fairy_island)

        val gotoMainBtn: Button = findViewById(R.id.gotoMain)
        val allIslandImg: ImageView = findViewById(R.id.allFairyIsland)
        val gotoFairytaleBtn: Button = findViewById(R.id.gotoFairyTale)

        gotoMainBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                this,
                0,
                0
            )

            startActivity(intent, options.toBundle())
        }

        gotoFairytaleBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}