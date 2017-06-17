var express = require('express');
var app = express();
var http = require('http');
var firebase = require("./firebase.js");

firebase.initFirebase();

var exampleEvent = {
    'eventId':1,
    'ort': 'Emil-Figge-Str. 42, 44137 Dortmund',
    'uhrzeit' : '16:50',
    'datum': '10.06.2017'
};

var events = [];

app.get('/health', function(req,res){
    res.send('Healthy');
});

app.get('/events',function(req,res){
    events.push(exampleEvent);
    res.send(JSON.stringify(events));
    console.log("Sent events... " + events.length);
});

app.get('/createEvent/:ort/:uhrzeit/:datum',function(req,res){
    var event = {
        'eventId': events.length,
        'ort': req.params.ort,
        'uhrzeit': req.params.uhrzeit,
        'datum': req.params.datum
    };
    events.push(event);
    res.send(JSON.stringify(events));
});

/**
 * Sends a notification to a device with the given token
 */
app.put('/sendNotification/:token/:message', function (req, res) {
    // TODO: Remove, this is for testing
    var payload = {
        notification: {
            title: "Test",
            body: req.params.message
        }
    };

    firebase.sendNotification(req.params.token, payload);
    res.json({status: 200});
});

/**
 * Updates the firebase token for the user with the given id
 */
app.put('/user/updateToken/:id/:token', function (req, res) {
    var id = req.params.id;
    var token = req.params.token;

    // TODO: update user in database
});

var server = app.listen(9999,'127.0.0.1',function(){
    var host = server.address().address;
    var port = server.address().port;
    console.log("Server for LUNCHMATES is working...");
});