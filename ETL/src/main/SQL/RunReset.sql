DROP sequence train_id_sequence;

CREATE SEQUENCE train_id_sequence INCREMENT BY 1 START WITH 1;

TRUNCATE TABLE stop;
TRUNCATE TABLE train;