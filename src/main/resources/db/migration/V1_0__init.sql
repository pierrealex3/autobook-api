
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
  possession_date TIMESTAMP NOT NULL DEFAULT NOW(),
  PRIMARY KEY (user_id, vehicle_id, possession_date),
  CONSTRAINT fk_user
    FOREIGN KEY (user_id)
      REFERENCES api_user(user_id)
      ON DELETE CASCADE,
  CONSTRAINT fk_vehicle_1
    FOREIGN KEY (vehicle_id)
      REFERENCES vehicle(id)
      ON DELETE CASCADE
);


/************************************/
/* Appointment                      */
/************************************/

CREATE TABLE IF NOT EXISTS appointment (
  id SERIAL PRIMARY KEY,
  xid VARCHAR(26) NOT NULL UNIQUE,
  title VARCHAR(100) NOT NULL,
  note VARCHAR(200),
  date DATE,
  time TIME,
  vehicle_id INTEGER,
  CONSTRAINT fk_vehicle_3
    FOREIGN KEY (vehicle_id)
      REFERENCES vehicle(id)
);

CREATE TABLE IF NOT EXISTS appointment_note (
  id SERIAL PRIMARY KEY,
  note VARCHAR(500),
  appointment_id INTEGER,
  CONSTRAINT fk_appointment
    FOREIGN KEY (appointment_id)
      REFERENCES appointment(id)
);

CREATE TABLE IF NOT EXISTS appointment_work_item (
  id SERIAL PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  appointment_id INTEGER,
  CONSTRAINT fk_appointment
    FOREIGN KEY (appointment_id)
      REFERENCES appointment(id)
);

CREATE TABLE IF NOT EXISTS appointment_work_item_labor (
  id SERIAL PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  cost NUMERIC(10, 2) DEFAULT 0.00 NOT NULL,
  hours_worked NUMERIC(6, 2),
  appointment_work_item_id INTEGER,
  CONSTRAINT fk_work_item
    FOREIGN KEY (appointment_work_item_id)
      REFERENCES appointment_work_item(id)
);


/************************************/
/* Piece                            */
/************************************/
CREATE TABLE IF NOT EXISTS piece (
  id SERIAL PRIMARY KEY,
  xid VARCHAR(26) NOT NULL UNIQUE,
  part_number VARCHAR(100) NOT NULL,
  brand VARCHAR(20),
  model VARCHAR(20),
  category VARCHAR(20) NOT NULL,
  tag VARCHAR(20),
  km_life INTEGER,
  days_life INTEGER /* the expected time life (in days) of the piece */
);

CREATE TABLE IF NOT EXISTS piece_operation (
  id SERIAL PRIMARY KEY,
  category VARCHAR(20) NOT NULL,
  cost NUMERIC(10, 2) DEFAULT 0.00 NOT NULL,
  performed_at TIMESTAMP NOT NULL,
  piece_id INTEGER,
  CONSTRAINT fk_piece_id
    FOREIGN KEY (piece_id)
      REFERENCES piece(id)
      ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS piece_operation_buy (
  id INTEGER PRIMARY KEY,
  CONSTRAINT fk_id
    FOREIGN KEY (id)
      REFERENCES piece_operation(id)
      ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS piece_operation_install (
  id INTEGER PRIMARY KEY,
  CONSTRAINT fk_id
    FOREIGN KEY (id)
      REFERENCES piece_operation(id)
      ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS piece_operation_repair (
  id INTEGER PRIMARY KEY,
  CONSTRAINT fk_id
    FOREIGN KEY (id)
      REFERENCES piece_operation(id)
      ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS piece_operation_sell (
  id INTEGER PRIMARY KEY,
  CONSTRAINT fk_id
    FOREIGN KEY (id)
      REFERENCES piece_operation(id)
      ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS piece_operation_thrash (
  id INTEGER PRIMARY KEY,
  CONSTRAINT fk_id
    FOREIGN KEY (id)
      REFERENCES piece_operation(id)
      ON DELETE CASCADE
);

/************************************/
/* Piece Operation AssociationS     */
/************************************/
CREATE TABLE IF NOT EXISTS appointment_work_item_piece_operation_asso (
  appointment_work_item_id INTEGER,
  piece_operation_id INTEGER,
  PRIMARY KEY (appointment_work_item_id, piece_operation_id),
  CONSTRAINT fk_appointment_work_item
    FOREIGN KEY (appointment_work_item_id)
      REFERENCES appointment_work_item(id),
  CONSTRAINT fk_piece_operation
    FOREIGN KEY (piece_operation_id)
      REFERENCES piece_operation(id)
      ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS vehicle_piece_operation_asso (
  vehicle_id INTEGER,
  piece_operation_id INTEGER,
  odometer_km INTEGER,
  PRIMARY KEY (vehicle_id, piece_operation_id),
  CONSTRAINT fk_vehicle_4
    FOREIGN KEY (vehicle_id)
      REFERENCES vehicle(id),
  CONSTRAINT fk_piece_operation_2
    FOREIGN KEY (piece_operation_id)
      REFERENCES piece_operation(id)
      ON DELETE CASCADE
);

/************************************/
/* Piece: Tire                      */
/************************************/
CREATE TABLE IF NOT EXISTS static_tire_season (
  id SERIAL PRIMARY KEY,
  title_EN VARCHAR(20) NOT NULL,
  title_FR VARCHAR(20) NOT NULL
);

INSERT INTO static_tire_season(title_EN, title_FR) VALUES ('ALL-SEASON', 'QUATRE SAISONS');
INSERT INTO static_tire_season(title_EN, title_FR) VALUES ('WINTER', 'HIVER');
INSERT INTO static_tire_season(title_EN, title_FR) VALUES ('SUMMER', 'ÉTÉ');

CREATE TABLE IF NOT EXISTS piece_tire (
  id INTEGER PRIMARY KEY,
  tire_season_id INTEGER,
  CONSTRAINT fk_id
    FOREIGN KEY (id)
      REFERENCES piece(id),
  CONSTRAINT fk_tire_season
    FOREIGN KEY (tire_season_id)
      REFERENCES static_tire_season(id)
);

/************************************/
/* Piece: Wiper                     */
/************************************/
CREATE TABLE IF NOT EXISTS piece_wiper (
  id INTEGER PRIMARY KEY,
  CONSTRAINT fk_id
    FOREIGN KEY (id)
      REFERENCES piece(id)
);

/************************************/
/* Piece: Brake                     */
/************************************/
CREATE TABLE IF NOT EXISTS piece_brake (
  id INTEGER PRIMARY KEY,
  CONSTRAINT fk_id
    FOREIGN KEY (id)
      REFERENCES piece(id)
);
