var mysql = require("mysql");
var config = require("./config/db.json");

var con = mysql.createConnection({
    host: config.host,
    user: config.username,
    password: config.password,
    database: config.db
});

exports.updateToken = function (id, token, callback) {
    con.query("UPDATE Users SET token = ? WHERE id = ?;", [token, id], function (err, result) {
       callback(err, result);
    });
};


/* ##################################### POSITION ##################################### */

exports.positionAdd = function (x, y, author, time, callback) {
    con.query("INSERT INTO Positions(x, y, user, time) VALUES(?,?,?,?);",[x,y,author,time], function (err, result) {
        callback(err, result);
    });
};

exports.positionGetUsersLatest = function (user, callback) {
    con.query("SELECT * FROM Positions WHERE user = ? ORDER BY time DESC LIMIT 1;", [user], function (err, result) {
        callback(err, result);
    });
};

exports.positionGetLatestPositionOfParticipants = function (event, callback) {
    con.query("SELECT * FROM Positions WHERE id IN (SELECT max(p.id) FROM Positions p GROUP BY p.user HAVING p.user IN (SELECT ue.user FROM UsersEvents ue WHERE ue.event = ?));", [event], function (err, result) {
        callback(err, result);
    });
};

exports.positionGetById = function (id, callback) {
    con.query("SELECT * FROM Positions WHERE id = ?;", [id], function (err, result) {
        callback(err, result);
    });
};

/* ##################################### EVENTS ##################################### */

exports.eventAdd = function (name, x, y, time, author, callback) {
    con.query("INSERT INTO Events (name, x, y, date, author) VALUES(?,?,?,?,?);" , [name, x, y, time, author], function (err, result) {
        callback(err, result);
    });
};

exports.eventGetAll = function (callback) {
    con.query("SELECT * FROM Events LEFT JOIN Users u ON author = u.id WHERE date > now();", function (err, result) {
        callback(err, result);
    });
};

exports.eventGetByUser = function (user, callback) {
    con.query("SELECT * FROM Events WHERE date > now() AND author = ?;", [user], function (err, result) {
        callback(err, result);
    });
};

exports.eventGetParticipants = function (event, callback) {
    con.query("SELECT u.* FROM UsersEvents ue LEFT JOIN Users u ON ue.user = u.id WHERE ue.event = ?;", [event], function(err, result) {
        callback(err, result);
    });
};

exports.eventGetById = function (id, callback) {
    con.query("SELECT * FROM Events WHERE id = :id;", [id], function (err, result) {
        callback(err, result);
    });
};

/* ##################################### USER ##################################### */

exports.userGetEvents = function (user, callback) {
    con.query("SELECT * FROM Events WHERE author = ?;", [user], function (err, result) {
       callback(err, result);
    });
};

exports.userGetById = function (id, callback) {
    con.query("SELECT id, name, email FROM Users WHERE id = ?;", [id], function (err, result) {
        callback(err, result);
    });
};

exports.userPpdateSessionToken = function (id, sessionToken, callback) {
    con.query("UPDATE Users SET session_token = ? WHERE id = ?;", [sessionToken, id], function (err, result) {
        callback(err, result);
    });
};

exports.userAdd = function (name, email, token, sessionToken, callback) {
    con.query("INSERT INTO Users (name, email, token, session_token) VALUES(?, ?, ?, ?);", [name, email, token, sessionToken], function (err, data) {
        callback(err, data);
    });
};

exports.userDelete = function (id, callback) {
    con.query("DELETE FROM UsersEvents WHERE user = ? OR event IN (SELECT id FROM Events WHERE author = ?);", [id, id], function (err, data) {
        if(err) {
            callback(err);
        } else {
            con.query("DELETE FROM Positions WHERE user = ?;", [id], function (err, data) {
                if(err) {
                    callback(err);
                } else {
                    con.query("DELETE FROM Events WHERE author = ?;", [id], function (err, data) {
                        if(err) {
                            callback(err);
                        } else {
                            con.query("DELETE FROM Users WHERE id = ?;", [id], function (err, data) {
                                callback(err, data);
                            });
                        }
                    });
                }
            });
        }
    });
};

/* ##################################### UsersEvents ##################################### */
exports.usersEventsAdd = function (user, event, callback) {
    con.query("INSERT INTO UsersEvents(user, event) VALUES(?, ?);", [user, event], function (err, result) {
        callback(err, result);
    });
};

exports.usersEventsRemove = function (user, event, callback) {
    con.query("REMOVE FROM UsersEvents WHERE event = ? AND user = ?;", [event, user], function (err, result) {
        callback(err, result);
    });
};

exports.usersEventsGetById = function (id, callback) {
    con.query("SELECT * FROM UsersEvents WHERE id = ?;", [id], function (err, result) {
        callback(err, result);
    });
};