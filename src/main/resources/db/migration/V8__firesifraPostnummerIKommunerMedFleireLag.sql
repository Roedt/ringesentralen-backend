ALTER TABLE postnummerIKommunerMedFleireLag CHANGE postnummerFra postnummerFra varchar(11);
ALTER TABLE postnummerIKommunerMedFleireLag CHANGE postnummerTil postnummerTil varchar(11);

UPDATE postnummerIKommunerMedFleireLag SET postnummerFra = LPAD(postnummerFra, 4, '0') WHERE postnummerFra != -1;
UPDATE postnummerIKommunerMedFleireLag SET postnummerTil = LPAD(postnummerTil, 4, '0') WHERE postnummerTil != -1;