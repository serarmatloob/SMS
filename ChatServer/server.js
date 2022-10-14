// Chat Server
const express = require('express')
const bodyParser = require('body-parser')
const { Server } = require('socket.io')

var app = express()

app.use(bodyParser.urlencoded({ extended: true }))
app.use(bodyParser.json())

var server = app.listen(3000,()=>{
    console.log('Server is running on port 3000')
})

// Construct a socket
const socketIO = new Server(server)

// Bind the socket to your created http server.
socketIO.listen(server)

socketIO.on('connection',function(socket) {
    console.log(`Connection : SocketId = ${socket.id}`)

    var userName = '';
    
    socket.on('subscribe', function(data) {
        console.log('subscribe invoked')
        const room_data = JSON.parse(data)
        userName = room_data.userName;
        const roomName = room_data.roomName;
    
        socket.join(`${roomName}`)
        console.log(`Username : ${userName} joined Room Name : ${roomName}`)
        
        socketIO.to(`${roomName}`).emit('newUserToChatRoom',userName);
    })

    socket.on('unsubscribe',function(data) {
        console.log('unsubscribe invoked')
        const room_data = JSON.parse(data)
        const userName = room_data.userName;
        const roomName = room_data.roomName;
    
        console.log(`Username : ${userName} leaved Room Name : ${roomName}`)
        socket.broadcast.to(`${roomName}`).emit('userLeftChatRoom',userName)
        socket.leave(`${roomName}`)
    })

    socket.on('newMessage',function(data) {
        console.log('newMessage invoked')

        const messageData = JSON.parse(data)
        const messageContent = messageData.messageContent
        const roomName = messageData.roomName

        console.log(`[Room Number ${roomName}] ${userName} : ${messageContent}`)

        const chatData = {
            userName : userName,
            messageContent : messageContent,
            roomName : roomName
        }
        socket.broadcast.to(`${roomName}`).emit('updateChat',JSON.stringify(chatData))
    })

    socket.on('disconnect', function () {
        console.log("One of sockets disconnected from the server.")
    });
})

module.exports = server;