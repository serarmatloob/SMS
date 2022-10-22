package com.matloob.sms.model

enum class MessageType(val index: Int) {
    CHAT_MINE(0), CHAT_PARTNER(1);
}

enum class EventType {
    SUBSCRIBE, UNSUBSCRIBE, NEW_MESSAGE, UPDATE_CHAT;
}