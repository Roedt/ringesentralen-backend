
INSERT INTO `lokallag` (`id`, `navn`, `fylke`) VALUES
(99999, 'Organisasjon1 sentralt', -1);

INSERT INTO `person` (`fornavn`, `etternavn`, `telefonnummer`, `postnummer`, `email`, `fylke`, `groupID`, `lokallag`, `hypersysID`, `kilde`) VALUES
('Systembruker', 'Frontend', '+4711223344', -1, null, -1, 4, -1, -2, 'Systembruker');

INSERT INTO `ringer` (`personId`) VALUES
((select id from `person` where fornavn='Systembruker' and etternavn='Frontend'));


INSERT INTO `person` (`fornavn`, `etternavn`, `telefonnummer`, `postnummer`, `email`, `fylke`, `groupID`, `oppretta`, `lokallag`, `kilde`) VALUES
('Telefonnummer som ikke', 'Er inne i systemet', '-1',	-1,	NULL,	-1,	1,	'2020-08-22 23:29:09', -1, 'Systembruker');


INSERT INTO `person` (`fornavn`, `etternavn`, `telefonnummer`, `postnummer`, `email`, `fylke`, `groupID`, `oppretta`, `lokallag`, `kilde`) VALUES
('Donald', ' Duck',	'+4712345678',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 99999, 'Verva'),
('Hetti', ' Duck',	'+4712345677',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 99999, 'Verva'),
('Letti', ' Duck',	'+4712345679',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Netti', ' Duck',	'+4712345676',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Klodrik', ' Duck',	'+4712345675', 1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Anton', ' Duck',	'+4712345674',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Bestemor', ' Duck',	'+4712345673',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Skrue', 'McDuck',	'+4712345672',	3050,	NULL,	6,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Gulbrand', 'Gråstein',	'+4712345671',	3050,	NULL,	6,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Spøkelseskladden', '',	'+4712345670',	3050,	NULL,	6,	1,	'2020-08-22 23:29:09', 1, 'Verva');

INSERT INTO `person` (`fornavn`, `etternavn`, `telefonnummer`, `postnummer`, `email`, `fylke`, `groupID`, `oppretta`, `lokallag`, `kilde`) VALUES
('Aster', 'ix',	'+4722345678',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Obel', 'ix',	'+4722345677',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Idef', 'ix',	'+4722345679',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Majest', 'ix',	'+4722345676',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Miracul', 'ix',	'+4722345675',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Hermet', 'ix',	'+4722345665',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Trubadur', 'ix',	'+4722346676',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Barometr', 'ix',	'+4722346677',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Gode', 'mine',	'+4722346678',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Senil', 'ix',	'+4722346679',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Armam', 'ix',	'+4722345680',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Lillef', 'ix',	'+4722345681',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Remoul', 'adine',	'+4722345682',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Tragicom', 'ix',	'+4722345683',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva');

-- --------------------------------------------------------

INSERT INTO `frivillig` (personId, registrertTidspunkt, alleredeAktivILokallag, medlemIRoedt, spesiellKompetanse, andreTingDuVilBidraMed, fortellLittOmDegSelv) VALUES
((SELECT id from person where fornavn='Aster' and etternavn='ix'), '2022-03-06 14:59:38', true, 'ja', 'rask og effektiv', 'bidrar med alt mogleg', 'frå Gallia'),
((SELECT id from person where fornavn='Obel' and etternavn='ix'), '2022-03-06 14:59:38', true, 'Vilbli', 'stor og sterk', 'rydde veg', 'også frå Gallia');

INSERT INTO `aktivitetForFrivillig` (frivillig_id, aktivitet) VALUES
((SELECT id from frivillig where spesiellKompetanse='rask og effektiv'), 'Ringing'),
((SELECT id from frivillig where spesiellKompetanse='rask og effektiv'), 'SoMe'),
((SELECT id from frivillig where spesiellKompetanse='stor og sterk'), 'Doerbanking');
