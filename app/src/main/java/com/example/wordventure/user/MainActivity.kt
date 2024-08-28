package com.example.wordventure.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.wordventure.BaseActivity
import com.example.wordventure.R
import com.example.wordventure.TokenManager
import com.example.wordventure.fairytale.FairyIslandActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val myIslandImg: ImageView = findViewById(R.id.myIsland)
        val gotoFairyIslandBtn: ImageButton = findViewById(R.id.gotoFairyIsland)

        if(TokenManager.isLoggedIn(this)) {
            gotoFairyIslandBtn.setOnClickListener {
                val intent = Intent(this, FairyIslandActivity::class.java)
                startActivity(intent)
            }
        } else {
            gotoFairyIslandBtn.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}