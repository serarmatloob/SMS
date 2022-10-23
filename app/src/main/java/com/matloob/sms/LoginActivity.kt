package com.matloob.sms

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun login(view: View) {
        val nameEditText: EditText = findViewById(R.id.editTextTextPersonName)
        val serverAddressEditText: EditText = findViewById(R.id.serverAddress)

        val serverAddress = serverAddressEditText.text.toString()
        val name = nameEditText.text.toString()

        if (name.isNotEmpty() && serverAddress.isNotEmpty()) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(USERNAME, name)
            intent.putExtra(SERVER_ADDRESS, serverAddress)
            startActivity(intent)
        }
    }
}