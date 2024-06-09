ALTER TABLE person DROP CONSTRAINT person_ibfk_4;
ALTER TABLE verving DROP CONSTRAINT verving_ibfk_1;

ALTER TABLE postnummer ALTER COLUMN Postnummer TYPE varchar(255);
ALTER TABLE person ALTER COLUMN postnummer TYPE varchar(255);
ALTER TABLE verving ALTER COLUMN postnummer TYPE varchar(255);

ALTER TABLE verving ADD CONSTRAINT fk_verving_postnummer FOREIGN KEY (postnummer) REFERENCES postnummer(Postnummer);
ALTER TABLE person ADD CONSTRAINT fk_person_postnummer FOREIGN KEY (postnummer) REFERENCES postnummer(Postnummer);