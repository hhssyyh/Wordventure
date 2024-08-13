package com.example.wordventure

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.wordventure.fairytale.FairyIslandActivity
import com.example.wordventure.user.LoginActivity
import com.example.wordventure.user.RegisterActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val gotoLoginBtn: Button = findViewById(R.id.gotoLogin)
        val gotoRegisterBtn: Button = findViewById(R.id.gotoRegister)
        val gotoFairyIslandBtn: Button = findViewById(R.id.gotoFairyIsland)
        val myIslandImg: ImageView = findViewById(R.id.myIsland)

        if(TokenManager.isLoggedIn(this)) {
            gotoLoginBtn.visibility = View.GONE
            gotoRegisterBtn.visibility = View.GONE
        }

        myIslandImg.visibility = View.INVISIBLE
        myIslandImg.post {
            // 줌인하면서 나타나기
            myIslandImg.visibility = View.VISIBLE
            myIslandImg.scaleX = 0.5f
            myIslandImg.scaleY = 0.5f
            myIslandImg.alpha = 0f

            myIslandImg.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1f)
                .setDuration(300)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
        }

        gotoLoginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        gotoRegisterBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        gotoFairyIslandBtn.setOnClickListener {
            val intent = Intent(this, FairyIslandActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                this,
                0,
                R.anim.zoom_out
            )

            startActivity(intent, options.toBundle())
        }
    }
}