ALTER TABLE postnummer DROP CONSTRAINT postnummer_ibfk_1;

ALTER TABLE kommune ALTER COLUMN nummer TYPE varchar(4);
ALTER TABLE postnummer ALTER COLUMN KommuneKode TYPE varchar(4);

UPDATE kommune SET nummer = LPAD(nummer, 4, '0') WHERE nummer != '-1';
UPDATE postnummer SET KommuneKode = LPAD(KommuneKode, 4, '0') WHERE KommuneKode != '-1';

ALTER TABLE postnummer ADD CONSTRAINT fk_postnummer_kommune FOREIGN KEY (KommuneKode) REFERENCES kommune(nummer);