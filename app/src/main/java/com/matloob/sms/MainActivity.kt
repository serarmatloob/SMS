package com.matloob.sms

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.matloob.sms.model.EventType
import com.matloob.sms.model.MessageType
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var chatRoomAdapter: ChatRoomAdapter
    private lateinit var username: String
    private lateinit var serverAddress: String
    private var mSocket: Socket? = null

    private val gson: Gson = Gson()
    private val chatList: ArrayList<Message> = arrayListOf();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        username = intent.extras?.getString(USERNAME)!!
        serverAddress = intent.extras?.getString(SERVER_ADDRESS)!!

        recyclerView = findViewById(R.id.recyclerView)

        chatRoomAdapter = ChatRoomAdapter(this, chatList);
        recyclerView.adapter = chatRoomAdapter;

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val socketUri = replaceURl(serverAddress)
                Log.d(TAG, "Connecting to $socketUri")
                mSocket = IO.socket(socketUri)
                mSocket?.connect()
                mSocket?.on(Socket.EVENT_CONNECT, onConnect)
                mSocket?.on(EventType.UPDATE_CHAT.toString(), onUpdateChat)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "Failed to connect $e")
            }
        }
    }

    private fun replaceURl(ip: String) = "http://$ip:3000"

    var onUpdateChat = Emitter.Listener {
        val chat: Message = gson.fromJson(it[0].toString(), Message::class.java)
        chat.viewType = MessageType.CHAT_PARTNER.index
        addItemToRecyclerView(chat)
    }

    private var onConnect = Emitter.Listener {
        Log.d(TAG, "ID: " + mSocket?.id())
        val data = InitialData(username, ROOM_NAME)
        val jsonData = gson.toJson(data)
        mSocket?.emit(EventType.SUBSCRIBE.toString(), jsonData)
        connected()
    }

    private fun addItemToRecyclerView(message: Message) {
        runOnUiThread {
            chatList.add(message)
            chatRoomAdapter.notifyItemInserted(chatList.size)
            recyclerView.scrollToPosition(chatList.size - 1) //move focus on last message
        }
    }

    private fun connected() {
        runOnUiThread {
            Toast.makeText(applicationContext, "Connected to chat!", Toast.LENGTH_SHORT).show()
        }
    }

    fun sendMessage(view: View) {
        if (mSocket != null) {
            when (view.id) {
                R.id.sendMessageButton -> {
                    val messageEditText = findViewById<EditText>(R.id.messageEditText)
                    val content = messageEditText.text.toString()
                    messageEditText.text.clear()
                    val message = Message(username, content, ROOM_NAME, MessageType.CHAT_MINE.index)
                    val jsonData = gson.toJson(message)
                    mSocket?.emit(EventType.NEW_MESSAGE.toString(), jsonData)

                    addItemToRecyclerView(message)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val data = InitialData(username, ROOM_NAME)
        val jsonData = gson.toJson(data)
        mSocket?.emit(EventType.UNSUBSCRIBE.toString(), jsonData)
        mSocket?.disconnect()
    }
}