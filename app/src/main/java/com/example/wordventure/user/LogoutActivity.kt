package com.example.wordventure.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.wordventure.BaseActivity
import com.example.wordventure.TokenManager

class LogoutActivity: AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(TokenManager.isLoggedIn(this@LogoutActivity)) {
            TokenManager.clearToken(this@LogoutActivity)
        }

        startActivity(Intent(this@LogoutActivity, MainActivity::class.java))
    }
}