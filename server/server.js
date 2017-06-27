var express = require('express');
var app = express();
var http = require('http');
var firebase = require("./firebase.js");
var db = require('./sql.js');
var timer = require("./timer.js");

firebase.initFirebase();
timer.startNotificationTimer();

var exampleEvent = {
    'eventId':1,
    'ort': 'Emil-Figge-Str. 42, 44137 Dortmund',
    'uhrzeit' : '16:50',
    'datum': '10.06.2017'
};

var events = [];

/* ##################################### UTIL ##################################### */

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

app.get('/health', function(req,res){
    res.send('Healthy');
});

/* ##################################### EVENTS ##################################### */

/**
 * Adds an event to the database
 */
app.put('/event/add/:name/:x/:y/:time/:author', function (req, res) {
    var name = req.params.name;
    var x = req.params.x;
    var y = req.params.y;
    var time = new Date(req.params.time * 1000);
    var author = req.params.author;

    var callback = function (err, data) {
        if(err) {
            return res.json({status: 500, error: err});
        } else {
            var id = data.insertId;
            return res.json({status: 200, newId : id});
        }
    };

    db.eventAdd(name, x, y, time, author, callback);
});

app.get('/event/getById/:id', function (req, res) {
    var id = req.params.id;
    
    var callback = function (err, data) {
        if(err) {
            console.log(err);
            return res.json({stauts: 500, error: err});
        } else {
            return res.json(data);
        }
    };

    db.eventGetById(id, callback);
});

/**
 * Returns all active and existing events
 */
app.get('/event/getAll', function (req, res) {
    var callback = function (err, data) {
        if(err) {
            console.log(err);
            return res.json({stauts: 500, error: err});
        } else {
            return res.json(data);
        }
    };

    db.eventGetAll(callback);
});

/**
 * Returns all events created by a given user
 */
app.get('/event/getByUser/:id', function (req, res) {
    var user = req.params.id;

    var callback = function (err, data) {
        if(err) {
            console.log(err);
            return res.json({stauts: 500, error: err});
        } else {
            return res.json(data);
        }
    };

    db.eventGetByUser(user, callback);
});

/**
 * Returns all participants of an given event
 */
app.get('/event/getParticipants/:event', function (req, res) {
    var event = req.params.event;

    var callback = function (err, data) {
        if(err) {
            console.log(err);
            return res.json({status: 500, error: err});
        } else {
            return res.json(data);
        }
    };

    db.eventGetParticipants(event, callback);
});

app.get('/event/getCount', function(req,res){
    var callback = function(err,data){
        if(err){
            console.log(err);
            return res.json({status: 500, error: err});
        }else{
            return res.json(data);
        }
    };

    db.eventGetCount(event, callback);
});

/* ##################################### POSITION ##################################### */

/**
 * Adds a position to the database
 */
app.put('/position/add/:x/:y/:author', function (req, res){
    var x = req.params.x;
    var y = req.params.y;
    var author = req.params.author;

    var callback = function (err, data) {
        if(err) {
            return res.json({status: 500, error: err});
        } else {
            var id = data.insertId;
            return res.json({status: 200, newId : id});
        }
    };

    db.positionAdd(x, y, author, new Date(), callback);
});

/**
 * Returns the latest position of a given user
 */
app.get('/position/getUsersLatest/:user', function (req, res) {
    var user = req.params.user;

    var callback = function (err, data) {
        if(err) {
            console.log(err);
            return res.json({stauts: 500, error: err});
        } else {
            return res.json(data);
        }
    };

    db.positionGetUsersLatest(user, callback);
});

/**
 * Returns the latest position of all participants of a given event
 */
app.get('/position/getLatestPositionOfParticipants/:event', function (req, res) {
    var event = req.params.event;

    var callback = function (err, data) {
        if(err) {
            console.log(err);
            return res.json({stauts: 500, error: err});
        } else {
            return res.json(data);
        }
    };

    db.positionGetLatestPositionOfParticipants(event, callback);
});

/**
 * Returns the position with the given id
 */
app.get('/position/getById/:id', function (req, res) {
    var id = req.params.id;

    var callback = function (err, data) {
        if(err) {
            console.log(err);
            return res.json({stauts: 500, error: err});
        } else {
            return res.json(data);
        }
    };

    db.positionGetById(id, callback);
});

/* ##################################### USER ##################################### */

/**
 * Updates the firebase token for the user with the given id
 */
app.put('/user/updateToken/:id/:token', function (req, res) {
    var id = req.params.id;
    var token = req.params.token;

    var callback = function(err, data) {
        if(err) {
            return res.json({status: 500, error: err})
        } else {
            return res.json({status: 200});
        }
    };

    db.updateToken(id, token, callback);
});

/**
 * Returns all events of a given user
 */
app.get('/user/getEvents/:user', function (req, res) {
    var user = req.params.user;

    var callback = function (err, data) {
        if(err) {
            console.log(err);
            return res.json({stauts: 500, error: err});
        } else {
            return res.json(data);
        }
    };

    db.userGetEvents(user, callback);
});

/**
 * Returns an user identified by his id
 */
app.get('/user/getById/:id', function (req, res) {
    var id = req.params.id;

    var callback = function (err, data) {
        if(err) {
            console.log(err);
            return res.json({stauts: 500, error: err});
        } else {
            return res.json(data);
        }
    };

    db.userGetById(id, callback);
});

/**
 * Deletes all data associated with the given user
 */
app.get('/user/delete/:id', function (req, res) {
    var id = req.params.id;

    var callback = function (err, data) {
        if(err) {
            return res.json({status: 500, error: err})
        } else {
            return res.json({status: 200});
        }
    };

    db.userDelete(id, callback);
});

/* ##################################### UsersEvents ##################################### */

/**
 * Adds an user to an event
 */
app.put('/usersEvents/add/:user/:event', function (req, res) {
    var user = req.params.user;
    var event = req.params.event;

    var callback = function (err, data) {
        if(err) {
            return res.json({status: 500, error: err})
        } else {
            var id = data.insertId;
            return res.json({status: 200, newId : id});
        }
    };

    db.usersEventsAdd(user, event, callback);
});

/**
 * Removes an UsersEvents identified by it's user and event
 */
app.get('/usersEvents/remove/:user/:event', function (req, res) {
    var user = req.params.user;
    var event = req.params.event;

    var callback = function (err, data) {
        if(err) {
            return res.json({status: 500, error: err})
        } else {
            return res.json({status: 200});
        }
    };

    db.usersEventsRemove(user, event, callback);
});

/**
 * Returns the UsersEvents with the given id
 */
app.get('/usersEvents/getById/:id', function (req, res) {
    var id = req.params.id;

    var callback = function (err, data) {
        if(err) {
            return res.json({status: 500, error: err})
        } else {
            return res.json(data);
        }
    };

    db.usersEventsGetById(id, callback);
});

/* ##################################### ServerStart ##################################### */

var server = app.listen(9999,'127.0.0.1',function(){
    var host = server.address().address;
    var port = server.address().port;
    console.log("Server for LUNCHMATES is working...");
});