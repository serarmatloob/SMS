package com.matloob.sms

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatRoomAdapter(private val context: Context, private val chatList: ArrayList<Message>) :
    RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null

        when(viewType) {
            0 -> {
                view = LayoutInflater.from(context).inflate(R.layout.row_chat_user, parent, false)
            }

            1 -> {
                view = LayoutInflater.from(context).inflate(R.layout.row_chat_partner, parent, false)
            }
        }
        return ViewHolder(view!!)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun getItemViewType(position: Int): Int {
        return chatList[position].viewType
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val messageData = chatList[position]
        val userName = messageData.userName;
        val content = messageData.messageContent;
        when (messageData.viewType) {
            CHAT_MINE -> {
                holder.userName.text = userName
                holder.message.text = content
            }
            CHAT_PARTNER -> {
                holder.userName.text = userName
                holder.message.text = content
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.username)
        val message: TextView = itemView.findViewById(R.id.message)
    }

}