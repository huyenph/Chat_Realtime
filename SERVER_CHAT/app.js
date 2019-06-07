var express = require("express");
var app = express();
var server = require("http").createServer(app);
var io = require("socket.io").listen(server);
var fs = require("fs");
server.listen(process.env.PORT || 3000);

// for web
// app.get("/", function(req, res){
// 	res.sendFile(__dirname + "/index.html");	
// });

var usernames = [];


io.sockets.on('connection', function(socket) {
	console.log("device connected");

	socket.on('client_register', function(data) {
		var result = false;
		if (usernames.indexOf(data) > -1) {
			result = false;
			console.log("username exist: " + data);
		} else {
			result = true;
			usernames.push(data);
			console.log("new username: " + data);
		}
		socket.emit('server_register_result', {message: result});
	});
}); 