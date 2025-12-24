BEGIN;
INSERT INTO api_user(user_id) VALUES ('me@whoami.com');
INSERT INTO vehicle (brand, model, modelyear) VALUES ('audi', 'rs7', 2019);
INSERT INTO piece (title, category) VALUES ('Winter tires', 'TIRE');
INSERT INTO piece_tire (id, brand, model, tire_season_id) VALUES (1, 'Blizzak', 'Bridgestone', 2);
COMMIT;