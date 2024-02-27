
CREATE TABLE IF NOT EXISTS api_user (
  user_id VARCHAR(64) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS vehicle (
  id SERIAL PRIMARY KEY,
  brand VARCHAR(32) NOT NULL,
  model VARCHAR(32) NOT NULL,
  modelyear SMALLINT NOT NULL,
  tag VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS user_vehicle_asso (
  user_id VARCHAR(64),
  vehicle_id INTEGER,
  possession_date TIMESTAMP,
  PRIMARY KEY (user_id, vehicle_id, possession_date),
  CONSTRAINT fk_user
    FOREIGN KEY (user_id)
      REFERENCES api_user(user_id)
      ON DELETE cascade,
  CONSTRAINT fk_vehicle
    FOREIGN KEY (vehicle_id)
      REFERENCES vehicle(id)
      ON DELETE cascade
);