package com.matloob.sms

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editText: EditText = findViewById(R.id.editTextTextPersonName)

        editText.onDone {
            val name = editText.text.toString()
            if (name.isNotEmpty()) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(USERNAME, name)
                startActivity(intent)
            }
        }
    }
}