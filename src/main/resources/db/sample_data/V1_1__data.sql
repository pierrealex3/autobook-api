INSERT INTO api_user(user_id) VALUES ('plemire@nuance.com');
INSERT INTO api_user(user_id) VALUES ('fberleur@nuance.com');
INSERT INTO vehicle (brand, model, modelyear) VALUES ('BMW', '340i', 2021);
INSERT INTO vehicle (brand, model, modelyear) VALUES ('Audi', 'A4', 2013);
INSERT INTO vehicle (brand, model, modelyear) VALUES ('Toyota', 'RAV4', 2003);
INSERT INTO user_vehicle_asso (user_id, vehicle_id) VALUES ('plemire@nuance.com', 1);
INSERT INTO user_vehicle_asso (user_id, vehicle_id) VALUES ('plemire@nuance.com', 2);
INSERT INTO user_vehicle_asso (user_id, vehicle_id) VALUES ('fberleur@nuance.com', 3);