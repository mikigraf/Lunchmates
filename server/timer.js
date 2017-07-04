var interval = 60000;
var timer = require("timer");
var sql = require("./sql.js");
var firebase = require("./firebase.js");

exports.startNotificationTimer = function () {
    setInterval(callback, interval);
};

var callback = function () {
    var callback = function(err, events) {
        if(!err) {
            for(i = 0; i < events.length; i++) {
                var event = events[i];
                sql.eventUpdateNotificationSend(event['id'], null);

                var participants_cb = function (err, participants) {
                    if(!err) {
                        for(j = 0; j < participants.length; j++) {
                            var participant = participants[j];

                            var payload = {
                                data: {
                                    eventId: event['id']+""
                                },
                                notification: {
                                    title:"Lunchmates Erinnerung",
                                    body:"Ein Event steht in 15 Minuten an."
                                }
                            };

                            firebase.sendNotification(participant['token'], payload);
                        }
                    } else {
                        console.log(err);
                    }
                };

                sql.eventGetParticipants(event['id'], participants_cb);
            }
        } else {
            console.log(err);
        }
    };

    sql.eventGetNotification(callback);
};
