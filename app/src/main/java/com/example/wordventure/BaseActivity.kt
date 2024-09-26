package com.example.wordventure

import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.wordventure.fairytale.FairyIslandActivity
import com.example.wordventure.user.LogoutActivity
import com.example.wordventure.user.MyIslandActivity

open class BaseActivity : AppCompatActivity() {

    private lateinit var windowManager: WindowManager
    private var floatingImageButton: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val notification = createNotification()  // 포그라운드 서비스에 필요한 알림 생성
//        startForeground(1, notification)

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // 특정 Activity에서는 오버레이 버튼을 표시하지 않음
        if (shouldShowOverlayButton()) {
            setupOverlayButton()
        }
    }

    override fun onResume() {
        super.onResume()
        // Activity가 다시 활성화될 때 오버레이 버튼을 다시 설정
        if (shouldShowOverlayButton() && floatingImageButton == null) {
            setupOverlayButton()
        }
    }

    private fun setupOverlayButton() {
        // 이미지 버튼이 null인 경우에만 생성 및 설정
        if (floatingImageButton == null) {
            floatingImageButton = createOverlayButton()

            if (floatingImageButton != null) {
                val sizeInDp = 90
                val sizeInPx = (sizeInDp * resources.displayMetrics.density).toInt()

                val params = WindowManager.LayoutParams(
                    sizeInPx,
                    sizeInPx,
                    WindowManager.LayoutParams.TYPE_APPLICATION,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    android.graphics.PixelFormat.TRANSLUCENT
                )
                params.gravity = Gravity.TOP or Gravity.END
                params.x = 35
                params.y = 50

                if (floatingImageButton?.parent == null) {
                    windowManager.addView(floatingImageButton, params)
                }
            } else {
                Log.e("BaseActivity", "FloatingImageButton creation failed.")
            }
        }
    }

    private fun createOverlayButton(): ImageButton? {
        return try {
            ImageButton(this).apply {
                setImageResource(R.drawable.home_btn)
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                scaleType = android.widget.ImageView.ScaleType.FIT_CENTER
                setOnClickListener {
                    showPopupMenu(it)
                }
            }
        } catch (e: Exception) {
            Log.e("BaseActivity", "Error creating overlay button", e)
            null
        }
    }

    private fun showPopupMenu(anchorView: View) {
        // 커스텀 레이아웃을 위한 PopupWindow 설정
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.home_menu, null)

        // PopupWindow 생성
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // 각 버튼에 대한 클릭 이벤트 처리
        popupView.findViewById<TextView>(R.id.action_home).setOnClickListener {
            popupWindow.dismiss()
            startActivity(Intent(this@BaseActivity, MyIslandActivity::class.java))
        }
        popupView.findViewById<TextView>(R.id.action_fairy).setOnClickListener {
            popupWindow.dismiss()
            startActivity(Intent(this@BaseActivity, FairyIslandActivity::class.java))
        }
        popupView.findViewById<TextView>(R.id.action_logout).setOnClickListener {
            popupWindow.dismiss()
            startActivity(Intent(this@BaseActivity, LogoutActivity::class.java))
        }

        // PopupWindow 위치 설정 및 표시
        val xOffset = -20  // 화면 오른쪽에서 떨어뜨림
        val yOffset = 0   // 아래로
        popupWindow.showAsDropDown(anchorView, xOffset, yOffset)
    }

    private fun removeOverlayButton() {
        floatingImageButton?.let {
            if (it.parent != null) {
                windowManager.removeView(it)
                floatingImageButton = null
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // Activity가 백그라운드로 가는 경우에만 버튼 제거
        removeOverlayButton()
    }
    override fun onDestroy() {
        super.onDestroy()
        // 파괴 시 오버레이 버튼 제거
        removeOverlayButton()
    }
    override fun onPause() {
        super.onPause()
        removeOverlayButton()  // 오버레이 버튼을 제거하는 메서드
    }

    // 이 메서드를 오버라이드하여 특정 액티비티에서 오버레이 버튼 비활성화 가능
    open fun shouldShowOverlayButton(): Boolean {
        return true
    }
}



