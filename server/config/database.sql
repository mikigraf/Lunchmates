CREATE TABLE Events
(
    name VARCHAR(100),
    id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    x VARCHAR(50),
    y VARCHAR(50),
    author INT(11),
    date DATETIME,
    notificationSend TINYINT(1) DEFAULT '0',
    CONSTRAINT Events_author_fk FOREIGN KEY (author) REFERENCES Users (id)
);
CREATE INDEX Events_author_fk ON Events (author);
CREATE TABLE Positions
(
    id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    x VARCHAR(50),
    y VARCHAR(50),
    time DATETIME,
    user INT(11) NOT NULL,
    CONSTRAINT Positions_user_fk FOREIGN KEY (user) REFERENCES Users (id)
);
CREATE INDEX Positions_user_fk ON Positions (user);

CREATE TABLE Users
(
    id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    token VARCHAR(255),
    session_token VARCHAR(255),
    name VARCHAR(50),
    email VARCHAR(100)
);

CREATE TABLE UsersEvents
(
    id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user INT(11) NOT NULL,
    event INT(11) NOT NULL,
    CONSTRAINT UsersEvents_user_fk FOREIGN KEY (user) REFERENCES Users (id),
    CONSTRAINT UsersEvents_event_fk FOREIGN KEY (event) REFERENCES Events (id)
);
CREATE INDEX UsersEvents_event_fk ON UsersEvents (event);
CREATE INDEX UsersEvents_user_fk ON UsersEvents (user);