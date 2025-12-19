BEGIN;
DELETE FROM appointment_note;
DELETE FROM appointment;
DELETE FROM api_user;
DELETE FROM vehicle;
COMMIT;