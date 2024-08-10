package com.example.wordventure

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import org.json.JSONObject

object TokenManager {
    private const val PREF_NAME = "MelonUser"
    private const val AUTH_TOKEN = "AUTH_TOKEN"

    // 토큰 저장
    fun saveToken(context: Context, token: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(AUTH_TOKEN, token)
        editor.apply()
    }

    // 토큰 불러오기
    fun getToken(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(AUTH_TOKEN, null)
    }

    // 토큰 삭제
    fun clearToken(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(AUTH_TOKEN)
        editor.apply()
    }

    // 로그인 여부 확인
    fun isLoggedIn(context: Context): Boolean {
        val token = getToken(context)
        return token != null && token.isNotEmpty()
    }


    //// 이거 할까 말까
    // 토큰 갱신
    fun refreshToken(context: Context) {
        val oldToken = getToken(context)

        oldToken?.let { token ->
            if (shouldRefreshToken(token)) {
                val newToken = requestNewTokenFromServer(token)
                if (newToken != null) {
                    saveToken(context, newToken)
                } else {
                    // 토큰 갱신 실패
                }
            } else {
                // 토큰 아직 유효
            }
        } ?: run {
            // oldToken == null인 경우
            clearToken(context)
        }
    }
    // 토큰 요청
    fun requestNewTokenFromServer(oldToken: String?): String? {
        return if (oldToken != null) {
            // 네트워크 요청
            "new_token_from_server"
        } else {
            null
        }
    }
    // 갱신 필요 여부
    fun shouldRefreshToken(jwt: String): Boolean {
        val payload = decodeJWT(jwt) ?: return true
        val exp = payload.optLong("exp", 0)
        val currentTime = System.currentTimeMillis() / 1000

        // 만료 시간 임박
        val refreshThreshold = 300 // 5분
        return exp - currentTime <= refreshThreshold
    }
    //토큰 디코딩
    fun decodeJWT(jwt: String): JSONObject? {
        return try {
            val parts = jwt.split(".")
            if (parts.size == 3) {
                val payload = parts[1]
                val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
                val decodedString = String(decodedBytes)
                JSONObject(decodedString)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}