ALTER TABLE person DROP CONSTRAINT fk_person_postnummer;
ALTER TABLE verving DROP CONSTRAINT fk_verving_postnummer;

UPDATE postnummer SET postnummer = LPAD(cast(postnummer as text), 4, '0') WHERE postnummer != '-1';
UPDATE person SET postnummer = LPAD(cast(postnummer as text), 4, '0') WHERE postnummer != '-1';
UPDATE verving SET postnummer = LPAD(cast(postnummer as text), 4, '0') WHERE postnummer != '-1';

ALTER TABLE verving ADD CONSTRAINT fk_verving_postnummer FOREIGN KEY (postnummer) REFERENCES postnummer(Postnummer);
ALTER TABLE person ADD CONSTRAINT fk_person_postnummer FOREIGN KEY (postnummer) REFERENCES postnummer(Postnummer);