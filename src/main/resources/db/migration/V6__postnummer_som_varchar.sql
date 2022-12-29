start transaction;
ALTER TABLE person DROP CONSTRAINT person_ibfk_4;
ALTER TABLE person DROP INDEX postnummer;
ALTER TABLE verving DROP CONSTRAINT verving_ibfk_1;
ALTER TABLE verving DROP INDEX postnummer;

ALTER TABLE postnummer CHANGE Postnummer Postnummer varchar(11);
ALTER TABLE person CHANGE postnummer postnummer varchar(11);
ALTER TABLE verving CHANGE postnummer postnummer varchar(11);

ALTER TABLE verving ADD CONSTRAINT fk_verving_postnummer FOREIGN KEY (postnummer) REFERENCES postnummer(Postnummer);
ALTER TABLE verving ADD INDEX ix_verving_postnummer (postnummer);
ALTER TABLE person ADD CONSTRAINT fk_person_postnummer FOREIGN KEY (postnummer) REFERENCES postnummer(Postnummer);
ALTER TABLE person ADD INDEX ix_person_postnummer (postnummer);

commit;