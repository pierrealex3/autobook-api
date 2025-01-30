BEGIN;
INSERT INTO api_user(user_id) VALUES ('me@whoami.com');
INSERT INTO vehicle (brand, model, modelyear) VALUES ('audi', 'rs7', 2019);
COMMIT;