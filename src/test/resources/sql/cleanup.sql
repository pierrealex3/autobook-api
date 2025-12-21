BEGIN;
DELETE FROM appointment_work_item_labor;
DELETE FROM appointment_work_item;
DELETE FROM appointment_note;
DELETE FROM appointment;
DELETE FROM api_user;
DELETE FROM vehicle;
COMMIT;