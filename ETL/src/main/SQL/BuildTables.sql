CREATE TABLE station(
    station_id      NUMBER PRIMARY KEY,
    code            VARCHAR2(3) UNIQUE NOT NULL
);

CREATE TABLE train(
    train_id        NUMBER PRIMARY KEY,
    train_number    NUMBER NOT NULL,
    depart_date     DATE NOT NULL,
    CONSTRAINT unique_train UNIQUE (train_number, depart_date)
);

CREATE TABLE route(
    route_id    NUMBER PRIMARY KEY,
    route_name  VARCHAR2(50)
);

CREATE TABLE stop(
    train_id    NUMBER,
    station_id  NUMBER,
    route_id    NUMBER,
    s_arrive    DATE,
    s_depart    DATE,
    a_arrive    DATE,
    a_depart    DATE,
    CONSTRAINT STOP_PK PRIMARY KEY (train_id, station_id, route_id),
    CONSTRAINT FK1 FOREIGN KEY (train_id) REFERENCES train(train_id),
    CONSTRAINT FK2 FOREIGN KEY (station_id) REFERENCES station(station_id),
    CONSTRAINT FK3 FOREIGN KEY (route_id) REFERENCES route
);


CREATE SEQUENCE route_id_sequence INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE train_id_sequence INCREMENT BY 1 START WITH 1;