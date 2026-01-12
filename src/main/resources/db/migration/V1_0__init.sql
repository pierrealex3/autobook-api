
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

create table if not exists appointment (
  id SERIAL primary key,
  xid VARCHAR(26) not null,
  title VARCHAR(100) not null,
  note VARCHAR(200),
  date DATE,
  time TIME,
  vehicle_id INTEGER,
  constraint fk_vehicle_3
    foreign key (vehicle_id)
      references vehicle(id)
);

/** B-tree index (default) **/
create index idx_appointment_xid ON appointment(xid);

create table if not exists appointment_note (
  id SERIAL primary key,
  note VARCHAR(500),
  appointment_id INTEGER,
  constraint fk_appointment
    foreign key (appointment_id)
      references appointment(id)
);

create table if not exists appointment_work_item (
  id SERIAL primary key,
  title VARCHAR(100) not null,
  appointment_id INTEGER,
  constraint fk_appointment
    foreign key (appointment_id)
      references appointment(id)
);

create table if not exists appointment_work_item_labor (
    id SERIAL primary key,
    title VARCHAR(100) not null,
    cost numeric(10, 2) default 0.00 not null,
    hours_worked numeric (6, 2),
    appointment_work_item_id INTEGER,
    constraint fk_work_item
      foreign key (appointment_work_item_id)
        references appointment_work_item(id)
);


/************************************/
/* Piece                            */
/************************************/
create table if not exists piece (
  id SERIAL primary key,
  title VARCHAR(100) not null,
  cost numeric(10, 2) default 0.00 not null,
  category VARCHAR(20),
  manufacturing_date TIMESTAMP
);

create table if not exists static_piece_operation (
  id SERIAL primary key,
  title_EN VARCHAR(20) not null,
  title_FR VARCHAR(20) not null
);

insert into static_piece_operation(title_EN, title_FR) values ('BUY', 'ACHAT');
insert into static_piece_operation(title_EN, title_FR) values ('INSTALL', 'INSTALLATION');
insert into static_piece_operation(title_EN, title_FR) values ('REPAIR', 'RÉPARATION');
insert into static_piece_operation(title_EN, title_FR) values ('REMOVAL', 'RETRAIT');

create table if not exists appointment_work_item_piece (
    id SERIAL primary key,
    category VARCHAR(20) not null,
    title VARCHAR(100) not null,
    appointment_work_item_id INTEGER,
    piece_id INTEGER,
    piece_operation_id INTEGER,
    constraint fk_work_item_2
      foreign key (appointment_work_item_id)
        references appointment_work_item(id),
    constraint fk_piece_id
      foreign key (piece_id)
        references piece(id)
);

create table if not exists appointment_work_item_piece_buy (
  id INTEGER primary key,
  cost numeric(10, 2) default 0.00 not null,
  constraint fk_id_appointment_work_item_piece_buy
      foreign key (id)
        references appointment_work_item_piece(id)
        ON DELETE CASCADE
);

create table if not exists appointment_work_item_piece_install (
  id INTEGER primary key,
  constraint fk_id_appointment_work_item_piece_install
      foreign key (id)
        references appointment_work_item_piece(id)
        ON DELETE CASCADE
);

create table if not exists appointment_work_item_piece_repair (
  id INTEGER primary key,
  constraint fk_id_appointment_work_item_piece_repair
      foreign key (id)
        references appointment_work_item_piece(id)
        ON DELETE CASCADE
);

create table if not exists appointment_work_item_piece_removal (
  id INTEGER primary key,
  constraint fk_id_appointment_work_item_piece_removal
      foreign key (id)
        references appointment_work_item_piece(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS piece_vehicle_asso (
  piece_id INTEGER,
  vehicle_id INTEGER,
  installation_date TIMESTAMP not null,
  removal_date TIMESTAMP,
  PRIMARY KEY (piece_id, vehicle_id, installation_date),
  CONSTRAINT fk_piece
    FOREIGN KEY (piece_id)
      REFERENCES piece(id),
  CONSTRAINT fk_vehicle_2
    FOREIGN KEY (vehicle_id)
      REFERENCES vehicle(id)
);

create table if not exists static_tire_season (
  id SERIAL primary key,
  title_EN VARCHAR(20) not null,
  title_FR VARCHAR(20) not null
);

insert into static_tire_season(title_EN, title_FR) values ('ALL-SEASON', 'QUATRE SAISONS');
insert into static_tire_season(title_EN, title_FR) values ('WINTER', 'HIVER');
insert into static_tire_season(title_EN, title_FR) values ('SUMMER', 'ÉTÉ');

create table if not exists piece_tire (
  id INTEGER primary key,
  tire_season_id integer,
  brand varchar(20),
  model varchar(20),
  constraint fk_id
    foreign key (id)
      references piece(id),
  constraint fk_tire_season
    foreign key (tire_season_id)
      references static_tire_season(id)
);





