package com.example.wordventure

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.wordventure.fairytale.FairyIslandActivity
import com.example.wordventure.user.LoginActivity
import com.example.wordventure.user.LogoutActivity
import com.example.wordventure.user.RegisterActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val myIslandImg: ImageView = findViewById(R.id.myIsland)
        val gotoFairyIslandBtn: ImageButton = findViewById(R.id.gotoFairyIsland)
        val logoutBtn: Button = findViewById(R.id.logout)

        if(TokenManager.isLoggedIn(this)) {
            gotoFairyIslandBtn.setOnClickListener {
                val intent = Intent(this, FairyIslandActivity::class.java)
//                val options = ActivityOptions.makeCustomAnimation(
//                    this,
//                    0,
//                    R.anim.zoom_out
//                )

//                startActivity(intent, options.toBundle())
                startActivity(intent)
            }
        } else {
            logoutBtn.visibility = View.GONE
            gotoFairyIslandBtn.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        logoutBtn.setOnClickListener {
            AlertDialog.Builder(this@MainActivity).apply {
                setTitle("알림")
                setMessage("로그아웃 하시겠습니까?")
                setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                    startActivity(Intent(this@MainActivity, LogoutActivity::class.java))
                    finish()
                }
                setNegativeButton("취소", null)
                create()
                show()
            }
        }
    }
}