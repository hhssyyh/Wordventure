package com.example.wordventure.user

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.wordventure.MainActivity
import com.example.wordventure.R
import com.example.wordventure.RetrofitClient
import com.example.wordventure.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private var isvalidId = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        val inputId: EditText = findViewById(R.id.id)
        val inputPasswd: EditText = findViewById(R.id.passwd)
        val checkIdBtn: Button = findViewById(R.id.chedkId)
        val registerBtn: Button = findViewById(R.id.register)
        val pwError: TextView = findViewById(R.id.pwError)
        val inputPasswd2: EditText = findViewById(R.id.passwd2)
        val pwError2: TextView = findViewById(R.id.pwError2)

        // 중복확인,회원가입 버튼 활성화/비활성화
        inputId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkIdBtn.isEnabled = !s.isNullOrEmpty()
                isvalidId = false // 아이디 변경되면 isvalidId false로 설정
                registerBtn.isEnabled = false // isvalidId false일 때 회원가입 버튼 비활성화
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // id 중복확인
        checkIdBtn.setOnClickListener {
            val id = inputId.text.toString()
            checkIdAvailability(id, registerBtn)
        }

        // 회원가입 버튼
        registerBtn.setOnClickListener {
            val id = inputId.text.toString()
            val passwd = inputPasswd.text.toString()
            val passwd2 = inputPasswd2.text.toString()
            val user = User(id, passwd)
            if(id.isEmpty() || passwd.isEmpty() || passwd2.isEmpty()) {
                showAlertDialog("아이디와 비밀번호 모두 입력해주세요.")
            } else if (isvalidId) {
                addUser(user)
            } else {
                showAlertDialog("아이디 중복 확인을 완료해주세요.")
            }
        }

        // 비밀번호 유효성 검사
        inputPasswd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (!isValidPassword(password)) {
                    pwError.text = "비밀번호는 8자 이상이어야 하며, 숫자와 특수문자를 포함해야 합니다."
                    pwError.visibility = TextView.VISIBLE
                } else {
                    pwError.visibility = TextView.GONE
                }
            }
        })

        // 비밀번호 일치 확인
        inputPasswd2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (inputPasswd.text.toString() != inputPasswd2.text.toString()) {
                    pwError2.text = "비밀번호가 일치하지 않습니다."
                    pwError2.visibility = TextView.VISIBLE
                } else {
                    pwError2.visibility = TextView.GONE
                }
            }
        })
    }

    // id 중복 확인
    private fun checkIdAvailability(id: String, registerBtn: Button) {
        val call = RetrofitClient.apiService.checkId(id)

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    val available = response.body() ?: false
                    if (available) {
                        isvalidId = true // 아이디가 사용 가능하면 isvalidId = true
                        registerBtn.isEnabled = true // 회원가입 버튼 활성화
                        showAlertDialog("사용 가능한 아이디 입니다.")
                    } else {
                        isvalidId = false
                        registerBtn.isEnabled = false
                        showAlertDialog("중복된 아이디 입니다.")
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "Server error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 회원가입 (첫번째 에피소드 해금됨)
    private fun addUser(user: User) {
        val call = RetrofitClient.apiService.addUsers(user)

        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    AlertDialog.Builder(this@RegisterActivity).apply {
                        setTitle("알림")
                        setMessage("회원가입이 완료되었습니다.")
                        setPositiveButton("확인") { dialog, _ ->
                            dialog.dismiss()
                            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                            finish()
                        }
                        create()
                        show()
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "Failed to add user", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 비밀번호 유효성 검사
    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[!@#\$%^&*()_+=-]).{8,}\$"
        val pattern = Regex(passwordPattern)
        return pattern.matches(password)
    }

    // 알림창 띄우기
    private fun showAlertDialog(msg: String) {
        AlertDialog.Builder(this).apply {
            setTitle("알림")
            setMessage(msg)
            setPositiveButton("확인", null)
            create()
            show()
        }
    }
}
