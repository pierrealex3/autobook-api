BEGIN;
INSERT INTO api_user(user_id) VALUES ('me@whoami.com');
INSERT INTO vehicle (brand, model, modelyear) VALUES ('audi', 'rs7', 2019);
INSERT INTO piece (xid, part_number, brand, model, category) VALUES ('manual-xid', 'WINTER-20555', 'Vortex', 'SuperWiper', 'WIPER');
INSERT INTO piece_wiper (id) VALUES (1);
COMMIT;