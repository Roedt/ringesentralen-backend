ALTER TABLE person DROP FOREIGN KEY fk_person_postnummer;
ALTER TABLE person DROP INDEX ix_person_postnummer;
ALTER TABLE verving DROP FOREIGN KEY fk_verving_postnummer;
ALTER TABLE verving DROP INDEX ix_verving_postnummer;

UPDATE postnummer SET postnummer = LPAD(Postnummer, 4, '0') WHERE postnummer != -1;
UPDATE person SET postnummer = LPAD(postnummer, 4, '0') WHERE postnummer != -1;
UPDATE verving SET postnummer = LPAD(postnummer, 4, '0') WHERE postnummer != -1;

ALTER TABLE verving ADD CONSTRAINT fk_verving_postnummer FOREIGN KEY (postnummer) REFERENCES postnummer(Postnummer);
ALTER TABLE verving ADD INDEX ix_verving_postnummer (postnummer);
ALTER TABLE person ADD CONSTRAINT fk_person_postnummer FOREIGN KEY (postnummer) REFERENCES postnummer(Postnummer);
ALTER TABLE person ADD INDEX ix_person_postnummer (postnummer);