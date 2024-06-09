ALTER TABLE postnummerIKommunerMedFleireLag ALTER COLUMN postnummerFra TYPE varchar(255);
ALTER TABLE postnummerIKommunerMedFleireLag ALTER COLUMN postnummerTil TYPE varchar(255);

UPDATE postnummerIKommunerMedFleireLag SET postnummerFra = LPAD(postnummerFra, 4, '0') WHERE postnummerFra != '-1';
UPDATE postnummerIKommunerMedFleireLag SET postnummerTil = LPAD(postnummerTil, 4, '0') WHERE postnummerTil != '-1';