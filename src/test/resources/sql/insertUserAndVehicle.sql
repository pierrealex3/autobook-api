BEGIN;
INSERT INTO api_user(user_id) VALUES ('me@whoami.com');
INSERT INTO vehicle (brand, model, modelyear) VALUES ('audi', 'rs7', 2019);
INSERT INTO piece (part_number, brand, model, category) VALUES ('WINTER-205-55', 'Bridgestone', 'Blizzak', 'TIRE');
INSERT INTO piece_tire (id, tire_season_id) VALUES (1, 2);
COMMIT;