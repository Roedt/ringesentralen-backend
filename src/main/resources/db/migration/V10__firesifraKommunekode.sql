ALTER TABLE postnummer DROP FOREIGN KEY postnummer_ibfk_1;
ALTER TABLE postnummer DROP INDEX KommuneKode;

ALTER TABLE kommune CHANGE nummer nummer varchar(4);
ALTER TABLE postnummer CHANGE KommuneKode KommuneKode varchar(4);

UPDATE kommune SET nummer = LPAD(nummer, 4, '0') WHERE nummer != -1;
UPDATE postnummer SET KommuneKode = LPAD(KommuneKode, 4, '0') WHERE KommuneKode != -1;

ALTER TABLE postnummer ADD CONSTRAINT fk_postnummer_kommune FOREIGN KEY (KommuneKode) REFERENCES kommune(nummer);
ALTER TABLE postnummer ADD INDEX ix_postnummer_kommune (KommuneKode);