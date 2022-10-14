package com.matloob.sms

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var mSocket: Socket
    private val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                mSocket = IO.socket("http://10.0.2.2:3000")
                mSocket.connect()
                mSocket.on(Socket.EVENT_CONNECT, onConnect)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "Failed to connect $e")
            }
        }
    }

    private var onConnect = Emitter.Listener {
        Log.d(TAG, "ID: " + mSocket.id())
        val data = initialData("Sam", "owe")
        val jsonData = gson.toJson(data)
        mSocket.emit("subscribe", jsonData)
        connected()
    }

    private fun connected() {
        runOnUiThread {
            Toast.makeText(applicationContext, "Connected to chat!", Toast.LENGTH_SHORT).show()
        }
    }

    fun sendMessage(view: View) {
        when (view.id) {
            R.id.sendMessageButton -> {
                val messageEditText = findViewById<EditText>(R.id.messageEditText)
                val message = messageEditText.text.toString()
                messageEditText.text.clear()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val data = initialData("Sam", "owe")
        val jsonData = gson.toJson(data)
        mSocket.emit("unsubscribe", jsonData)
        mSocket.disconnect()
    }
}