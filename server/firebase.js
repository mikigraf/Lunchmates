var firebase = require("firebase-admin");
var app;

exports.initFirebase = function() {
    // init firebase
    var serviceData = require("./config/service-account.json");
    app = firebase.initializeApp({
        credential: firebase.credential.cert(serviceData),
        databaseURL: "https://lunchmates-ddd8f.firebaseio.com"
    });

    console.log("Firebase app is running, name: "+app.name);
};


exports.sendNotification = function(token, payload) {
    firebase.messaging().sendToDevice(token, payload)
        .then(function(response) {
            console.log("Successfully sent message:", payload.notification.title);
        })
        .catch(function(error) {
            console.log("Error sending message:", error);
        });
};