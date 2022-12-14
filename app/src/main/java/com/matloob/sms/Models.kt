package com.matloob.sms

data class Message(
    val userName: String,
    var messageContent: String,
    val roomName: String,
    var viewType: Int
)

data class InitialData(val userName: String, val roomName: String)