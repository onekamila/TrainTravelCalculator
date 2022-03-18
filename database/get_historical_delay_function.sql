CREATE OR REPLACE FUNCTION GET_HISTORICAL_DEPARTURE_DELAY (
    origin_train_id IN NUMBER, 
    origin_station_id IN NUMBER, 
    dest_train_id IN NUMBER, 
    dest_station_id IN NUMBER)
    RETURN NUMBER IS
    v_origin_avg_delay NUMBER;
    v_dest_avg_delay NUMBER;
BEGIN
    SELECT AVG(a_depart - s_depart) 
    INTO v_origin_avg_delay 
    FROM Stop 
    WHERE train_id = origin_train_id
    AND station_id = origin_station_id;

    SELECT AVG(a_depart - s_depart) 
    INTO v_dest_avg_delay 
    FROM Stop 
    WHERE train_id = dest_train_id
    AND station_id = dest_station_id;

    RETURN (v_origin_avg_delay + v_dest_avg_delay) /2;
END GET_HISTORICAL_DEPARTURE_DELAY;
\

CREATE OR REPLACE FUNCTION GET_HISTORICAL_ARRIVAL_DELAY (
    origin_train_id IN NUMBER, 
    origin_station_id IN NUMBER, 
    dest_train_id IN NUMBER, 
    dest_station_id IN NUMBER)
    RETURN NUMBER IS
    v_origin_avg_delay NUMBER;
    v_dest_avg_delay NUMBER;
BEGIN
    SELECT AVG(a_arrive - s_arrive) 
    INTO v_origin_avg_delay 
    FROM Stop 
    WHERE train_id = origin_train_id
    AND station_id = origin_station_id;

    SELECT AVG(a_arrive - s_arrive) 
    INTO v_dest_avg_delay 
    FROM Stop 
    WHERE train_id = dest_train_id
    AND station_id = dest_station_id;

    RETURN (v_origin_avg_delay + v_dest_avg_delay) /2;
END GET_HISTORICAL_ARRIVAL_DELAY;