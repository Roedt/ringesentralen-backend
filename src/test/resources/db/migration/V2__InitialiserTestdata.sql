
INSERT INTO lokallag (id, navn, fylke) VALUES
(99999, 'Organisasjon1 sentralt', -1);

INSERT INTO person (fornavn, etternavn, telefonnummer, postnummer, email, fylke, groupID, lokallag, hypersysID, kilde) VALUES
('Systembruker', 'Frontend', '+4711223344', -1, null, -1, 4, -1, -2, 'Systembruker');

INSERT INTO ringer (personId) VALUES
((select id from person where fornavn='Systembruker' and etternavn='Frontend'));


INSERT INTO person (fornavn, etternavn, telefonnummer, postnummer, email, fylke, groupID, oppretta, lokallag, kilde) VALUES
('Telefonnummer som ikke', 'Er inne i systemet', '-1',	-1,	NULL,	-1,	1,	'2020-08-22 23:29:09', -1, 'Systembruker');


INSERT INTO person (fornavn, etternavn, hypersysID, telefonnummer, postnummer, email, fylke, groupID, oppretta, lokallag, kilde) VALUES
('Donald', ' Duck',	15424, '+4712345678',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 99999, 'Hypersys');

INSERT INTO person (fornavn, etternavn, telefonnummer, postnummer, email, fylke, groupID, oppretta, lokallag, kilde) VALUES
('Hetti', ' Duck',	'+4712345677',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 99999, 'Verva'),
('Letti', ' Duck',	'+4712345679',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Netti', ' Duck',	'+4712345676',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Klodrik', ' Duck',	'+4712345675', 1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Anton', ' Duck',	'+4712345674',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Bestemor', ' Duck',	'+4712345673',	1,	NULL,	-1,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Skrue', 'McDuck',	'+4712345672',	3050,	NULL,	6,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Gulbrand', 'Gråstein',	'+4712345671',	3050,	NULL,	6,	1,	'2020-08-22 23:29:09', 1, 'Verva'),
('Spøkelseskladden', '',	'+4712345670',	3050,	NULL,	6,	1,	'2020-08-22 23:29:09', 1, 'Verva');

INSERT INTO person (fornavn, etternavn, telefonnummer, postnummer, email, fylke, groupID, oppretta, lokallag, kilde) VALUES
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

INSERT INTO frivillig (personId, registrertTidspunkt, alleredeAktivILokallag, medlemIRoedt, spesiellKompetanse, andreTingDuVilBidraMed, fortellLittOmDegSelv) VALUES
((SELECT id from person where fornavn='Aster' and etternavn='ix'), '2022-03-06 14:59:38', true, 'Ja', 'rask og effektiv', 'bidrar med alt mogleg', 'frå Gallia'),
((SELECT id from person where fornavn='Obel' and etternavn='ix'), '2022-03-06 14:59:38', true, 'Vilbli', 'stor og sterk', 'rydde veg', 'også frå Gallia');

INSERT INTO aktivitetForFrivillig (frivillig_id, aktivitet) VALUES
((SELECT id from frivillig where spesiellKompetanse='rask og effektiv'), 'Ringing'),
((SELECT id from frivillig where spesiellKompetanse='rask og effektiv'), 'SoMe'),
((SELECT id from frivillig where spesiellKompetanse='stor og sterk'), 'Doerbanking');

-- --------------------------------------------------------

INSERT INTO lokallag (id, navn, hypersysID, fylke, sistOppdatert) VALUES (1000,'Organisasjon 1 Agder',23,-1,NULL),
(1001,'Organisasjon 1 Arendal',25,-1,NULL),(1002,'Organisasjon 1 Bergen',27,-1,NULL),(1003,'Organisasjon 1 Bodø',15,-1,NULL),
(1004,'Organisasjon 1 Gamle Oslo',7,-1,NULL),(1005,'Organisasjon 1 Grorud',6,-1,NULL),(1006,'Organisasjon 1 Hamar',18,-1,NULL),
(1007,'Organisasjon1 Harstad',34,-1,NULL),(1008,'Organisasjon 1 Innlandet',17,-1,NULL),(1009,'Organisasjon 1 Kristiansand',24,-1,NULL),
(1010,'Organisasjon1 Kristiansund',13,-1,NULL),(1011,'Organisasjon 1 Lillehammer',19,-1,NULL),(1012,'Organisasjon 1 Mo i Rana',16,-1,NULL),
(1013,'Organisasjon 1 Molde',12,-1,NULL),(1014,'Organisasjon 1 Møre og Romsdal',11,-1,NULL),(1015,'Organisasjon1 Moss',4,-1,NULL),
(1016,'Organisasjon1 Nittedal',3,-1,NULL),(1017,'Organisasjon 1 Nordland',14,-1,NULL),(1018,'Organisasjon1 Oslo',5,-1,NULL),
(1019,'Organisasjon 1 Rennebu',31,-1,NULL),(1020,'Organisasjon1 Rogaland',8,-1,NULL),(1021,'Organisasjon 1 Sandnes',10,-1,NULL),
(1022,'Organisasjon 1 Skien',21,-1,NULL),(1023,'Organisasjon 1 Sogndal',28,-1,NULL),(1024,'Organisasjon 1 Stavanger',9,-1,NULL),
(1025,'Organisasjon1 Telemark og Vestfold',20,-1,NULL),(1026,'Organisasjon 1 Tønsberg',22,-1,NULL),(1027,'Organisasjon 1 Tromsø',33,-1,NULL),
(1028,'Organisasjon 1 Troms og Finnmark',32,-1,NULL),(1029,'Organisasjon 1 Trøndelag',29,-1,NULL),(1030,'Organisasjon 1 Trondheim',30,-1,NULL),
(1031,'Organisasjon 1 Vestland',26,-1,NULL),(1032,'Organisasjon1 Viken',2,-1,NULL);