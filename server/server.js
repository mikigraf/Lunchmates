var express = require('express');
var app = express();
var http = require('http');


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



var server = app.listen(9999,'127.0.0.1',function(){
    var host = server.address().address;
    var port = server.address().port;
    console.log("Server for LUNCHMATES is working...");
});