package com.example.wordventure.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.wordventure.MainActivity
import com.example.wordventure.TokenManager

class LogoutActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AlertDialog.Builder(this@LogoutActivity).apply {
            setTitle("알림")
            setMessage("로그아웃 하시겠습니까?")
            setPositiveButton("확인") { dialog, _ ->
                if(TokenManager.isLoggedIn(this@LogoutActivity)) {
                    TokenManager.clearToken(this@LogoutActivity)
                }
                dialog.dismiss()
                startActivity(Intent(this@LogoutActivity, MainActivity::class.java))
                finish()
            }
            setPositiveButton("취소", null)
            create()
            show()
        }
    }
}