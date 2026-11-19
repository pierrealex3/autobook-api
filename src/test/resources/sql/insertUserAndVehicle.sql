BEGIN;
INSERT INTO api_user(user_id) VALUES ('me@whoami.com');
INSERT INTO vehicle (brand, model, modelyear) VALUES ('audi', 'rs7', 2019);
INSERT INTO piece (xid, part_number, brand, model, category) VALUES ('manual-xid', 'WINTER-20555', 'Vortex', 'SuperWiper', 'WIPER');
INSERT INTO piece_wiper (id) VALUES (currval(pg_get_serial_sequence('piece', 'id')));
INSERT INTO piece_operation (category, cost, performed_at, piece_id) VALUES ('BUY', 11.33, '2023-01-01 00:00:00', 1);
INSERT INTO piece_operation_buy (id)
VALUES (currval(pg_get_serial_sequence('piece_operation', 'id')));

INSERT INTO piece_operation_vehicle_link
(piece_operation_id, vehicle_id, odometer_km)
VALUES (
         currval(pg_get_serial_sequence('piece_operation', 'id')),
         currval(pg_get_serial_sequence('vehicle', 'id')),
         9999
       );
COMMIT;