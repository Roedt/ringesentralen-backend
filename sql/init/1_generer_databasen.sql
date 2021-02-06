SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";
SET NAMES utf8;


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `fylker` (
  `id` int(2) NOT NULL PRIMARY KEY,
  `name` varchar(60) NOT NULL
);

INSERT INTO `fylker` (`id`, `name`) VALUES
(-1, 'Udefinert fylke'),
(01, 'Østfold'),
(02, 'Akershus'),
(03, 'Oslo'),
(04, 'Hedmark'),
(05, 'Oppland'),
(06, 'Buskerud'),
(07, 'Vestfold'),
(08, 'Telemark'),
(09, 'Aust-Agder'),
(10, 'Vest-Agder'),
(11, 'Rogaland'),
(12, 'Hordaland'),
(14, 'Sogn og Fjordane'),
(15, 'Møre og Romsdal'),
(18, 'Nordland'),
(19, 'Troms'),
(20, 'Finnmark'),
(21, 'Svalbard'),
(30, 'Viken'),
(34, 'Innlandet'),
(38, 'Vestfold og Telemark'),
(42, 'Agder'),
(46, 'Vestland'),
(50, 'Trøndelag'),
(54, 'Troms og Finnmark');


CREATE TABLE IF NOT EXISTS `postnumber` (
  `postnumber` varchar(4) PRIMARY KEY NOT NULL,
  `postplace` varchar(250) NOT NULL,
  `countyID` int(2) NOT NULL,
  `municipalityID` int(2) NOT NULL,
  `municipality` varchar(30) NOT NULL,
  INDEX(`countyID`),
  FOREIGN KEY (`countyID`) REFERENCES `fylker`(`id`)
);

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `modus` (
  `id` int(2) PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(60) NOT NULL
);

INSERT INTO modus(name) VALUES ('korona');

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `callGroup` (
  `id` int(2) NOT NULL PRIMARY KEY,
  `name` varchar(60) NOT NULL
);

INSERT INTO callGroup VALUES (0, 'mangler info/samtykke før ringing');
INSERT INTO callGroup VALUES (1, 'klar til å ringes');
INSERT INTO callGroup VALUES (2, 'ferdigringt');
INSERT INTO callGroup VALUES (3, 'slett');
INSERT INTO callGroup VALUES (4, 'ugodkjent ringer');
INSERT INTO callGroup VALUES (5, 'ringer som aktivt ikke er godkjent');
INSERT INTO callGroup VALUES (6, 'godkjent ringer og relay-bruker');
INSERT INTO callGroup VALUES (7, 'trenger oppfølging');
INSERT INTO callGroup VALUES (8, 'ringer som kan godkjenne ringere i sitt lokallag');
INSERT INTO callGroup VALUES (9, 'admin');

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `lokallag` (
  `id` int(3) unsigned NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(100) NOT NULL UNIQUE
);

insert into `lokallag` (name) values 
('Raudt Alver'),
('Raudt Årdal'),
('Raudt Austrheim'),
('Raudt Bjørnafjorden'),
('Raudt Bømlo'),
('Raudt Bremanger'),
('Raudt Dalsfjorden'),
('Raudt Flora'),
('Raudt Gloppen'),
('Raudt Hå'),
('Raudt Høyanger'),
('Raudt Indre Sunnfjord'),
('Raudt Kvam'),
('Raudt Kvinnherad'),
('Raudt Lærdal'),
('Raudt Luster'),
('Raudt Ørsta'),
('Raudt Søre Sunnmøre'),
('Raudt Stord'),
('Raudt Stryn'),
('Raudt Time og Klepp'),
('Raudt Ulstein'),
('Raudt Vågsøy'),
('Raudt Vaksdal'),
('Raudt Valdres'),
('Raudt Voss'),
('Rødt'),
('Rødt Ålesund'),
('Rødt Alstahaug'),
('Rødt Alta'),
('Rødt Åmli'),
('Rødt Andøy'),
('Rødt Arendal'),
('Rødt Ås'),
('Rødt Ås Studentlag'),
('Rødt Asker'),
('Rødt Askøy'),
('Rødt Åsnes'),
('Rødt Aurskog-Høland'),
('Rødt Bærum'),
('Rødt Balsfjord'),
('Rødt Bamble'),
('Rødt Bergen Møhlenpris'),
('Rødt Bergen Nord'),
('Rødt Bergen Sentrum'),
('Rødt Bergen Sør'),
('Rødt Bergen Studentlag'),
('Rødt Bergen Vest'),
('Rødt Bodø'),
('Rødt Brønnøy'),
('Rødt Dalane'),
('Rødt Dønna'),
('Rødt Drammen'),
('Rødt Eidsvoll og Hurdal'),
('Rødt Elverum'),
('Rødt Farsund'),
('Rødt Fauske'),
('Rødt Flekkefjord'),
('Rødt Folldal'),
('Rødt Fredrikstad'),
('Rødt Frogn'),
('Rødt Froland'),
('Rødt Frøya'),
('Rødt Gjøvik og Land'),
('Rødt Gran'),
('Rødt Grimstad'),
('Rødt Hadsel'),
('Rødt Halden'),
('Rødt Hamar'),
('Rødt Hamarøy'),
('Rødt Hammerfest'),
('Rødt Harstad'),
('Rødt Haugaland'),
('Rødt Hemnes'),
('Rødt Hitra'),
('Rødt Holmestrand'),
('Rødt Horten'),
('Rødt Indre Østfold'),
('Rødt Kongsberg'),
('Rødt Kongsvinger og omegn'),
('Rødt Kragerø'),
('Rødt Kristiansand'),
('Rødt Kristiansund'),
('Rødt Larvik'),
('Rødt Leirfjord'),
('Rødt Levanger'),
('Rødt Lier'),
('Rødt Lillehammer'),
('Rødt Lillesand'),
('Rødt Lillestrøm'),
('Rødt Lindesnes'),
('Rødt Lørenskog'),
('Rødt Løten'),
('Rødt Lunner'),
('Rødt Lyngdal'),
('Rødt Målselv'),
('Rødt Malvik'),
('Rødt Måsøy'),
('Rødt Melhus'),
('Rødt Midt-Gudbrandsdalen'),
('Rødt Midt-Telemark'),
('Rødt Modum'),
('Rødt Molde'),
('Rødt Moss og omegn'),
('Rødt Nærøysund'),
('Rødt Namsos'),
('Rødt Narvik'),
('Rødt Nedre Romerike'),
('Rødt Nes'),
('Rødt Nesna'),
('Rødt Nesodden'),
('Rødt Nittedal'),
('Rødt Nordre Follo'),
('Rødt Notodden'),
('Rødt Odal'),
('Rødt Øksnes'),
('Rødt Orkland'),
('Rødt Ørland'),
('Rødt Oslo Alna'),
('Rødt Oslo Arbeiderlag'),
('Rødt Oslo Bjerke'),
('Rødt Oslo Gamle Oslo'),
('Rødt Oslo Grorud'),
('Rødt Oslo Grünerløkka'),
('Rødt Oslo Jernbane og Sporveier'),
('Rødt Oslo kommune'),
('Rødt Oslo Kvinnepolitisk'),
('Rødt Oslo Nordre Aker'),
('Rødt Oslo Nordstrand'),
('Rødt Oslo Østensjø'),
('Rødt Oslo Sagene'),
('Rødt Oslo Skole og Barnehage'),
('Rødt Oslo Solidaritetslag'),
('Rødt Oslo Søndre Nordstrand'),
('Rødt Oslo St. Hanshaugen'),
('Rødt Oslo Stovner'),
('Rødt Oslo Studentlag'),
('Rødt Oslo Vest'),
('Rødt Østre Toten'),
('Rødt Øvre Eiker'),
('Rødt Øygarden'),
('Rødt Porsgrunn'),
('Rødt Råde'),
('Rødt Rana'),
('Rødt Ringerike'),
('Rødt Ringsaker'),
('Rødt Risør'),
('Rødt Røst'),
('Rødt Salangen'),
('Rødt Saltdal'),
('Rødt Sandefjord'),
('Rødt Sandnes'),
('Rødt Sarpsborg og omegn'),
('Rødt Setesdal'),
('Rødt Skaun'),
('Rødt Skien'),
('Rødt Skjervøy'),
('Rødt Sola'),
('Rødt Sømna'),
('Rødt Sør-Varanger'),
('Rødt Sørfold'),
('Rødt Sortland'),
('Rødt Stange'),
('Rødt Stavanger'),
('Rødt Steigen'),
('Rødt Steinkjer'),
('Rødt Stjørdal'),
('Rødt Strand'),
('Rødt Sunndal'),
('Rødt Tønsberg og Færder'),
('Rødt Tromsø'),
('Rødt Tromsø Studentlag'),
('Rødt Tvedestrand'),
('Rødt Ullensaker og Nannestad'),
('Rødt Ullensvang'),
('Rødt Vadsø'),
('Rødt Vågan'),
('Rødt Våler i Østfold'),
('Rødt Vefsn'),
('Rødt Vega'),
('Rødt Vegårshei'),
('Rødt Vennesla'),
('Rødt Verdal'),
('Rødt Vestby'),
('Rødt Vestre Toten'),
('Rødt Vestvågøy'),
('Røros'),
('Test-lokallag'),
('Trondheim Helse- og Sosialpolitisk lag'),
('Trondheim Østbyen'),
('Trondheim Strinda'),
('Trondheim Universitetslag'),
('Trondheim-Fagliglag'),
('Trondheim-Sentrum'),
('Uten lag Finnmark'),
('Uten lag Hedmark'),
('uten lag Hordaland'),
('Uten lag Møre og Romsdal'),
('Uten lag Nordland'),
('Uten lag Oppland'),
('Uten lag Rogaland'),
('Uten lag Sogn og Fjordane'),
('Uten lag Telemark'),
('Uten lag Troms'),
('Uten lag Trøndelag'),
('Uten lag Vestfold'),
('Uten lag Vestfold og Telemark'),
('Utland'),
('Organisasjon1 sentralt');

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `person` (
  `id` int(6) unsigned NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `hypersysID` int(6) DEFAULT NULL,
  `givenName` varchar(60) DEFAULT NULL,
  `familyName` varchar(60) DEFAULT NULL,
  `phone` varchar(15) NOT NULL UNIQUE,
  `postnumber` varchar(4) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `countyID` int(2) DEFAULT -1 NOT NULL,
  `groupID` int(2) DEFAULT NULL,
  `userCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastCall` int(11) NOT NULL DEFAULT '0',
  `lokallag` int(3) unsigned DEFAULT NULL,
  FOREIGN KEY (`groupID`) REFERENCES `callGroup` (`id`),
  FOREIGN KEY(`countyID`) REFERENCES `fylker` (`id`),
  FOREIGN KEY(`lokallag`) REFERENCES `lokallag` (`id`),
  FOREIGN KEY(`postnumber`) REFERENCES `postnumber` (`postnumber`),
  INDEX (`groupID`),
  INDEX (`countyID`),
  INDEX (`phone`),
  INDEX (`lokallag`),
  INDEX (`postnumber`),
  INDEX (`hypersysID`)
);

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `ringer` (
  `id` int(6) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `userCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `personId` int(6) unsigned NOT NULL UNIQUE,
  FOREIGN KEY (`personId`) REFERENCES `person` (`id`),
  INDEX(`personId`)
);

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `oppfoelgingKorona` (
  `personId` int(6) unsigned PRIMARY KEY NOT NULL,
  `koronaprogram` tinyint(1) DEFAULT NULL,
  `merAktiv` tinyint(1) DEFAULT NULL,
  `valgkampsbrev` tinyint(1) DEFAULT NULL,
  `vilIkkeBliRingt` tinyint(1) DEFAULT NULL,
  FOREIGN KEY (`personId`) REFERENCES `person` (`id`),
  INDEX (`personId`)
);

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `result` (
  `id` int(1) NOT NULL PRIMARY KEY,
  `name` varchar(200) NOT NULL,
  `displaytext`varchar(200) NOT NULL,
  `skalSkjules` bit DEFAULT FALSE,
  `svarte` BIT DEFAULT TRUE
);

INSERT INTO result (id, name, displaytext, svarte) VALUES (0, 'Ikke svar', 'Ikke svar', 0);
INSERT INTO result (id, name, displaytext) VALUES (1, 'Vil ikke ringes før valget, men gjerne etterpå', 'Vil ikke ringes før valget, men gjerne etterpå');
INSERT INTO result (id, name, displaytext) VALUES (2, 'Trenger ikke å bli oppringt igjen (slettes fra alle lister)', 'Trenger ikke å bli oppringt igjen');
INSERT INTO result (id, name, displaytext) VALUES (3, 'Trenger oppfølging av toppkandidat (husk å skrive stikkord om tema i kommentarfeltet)', 'Trenger oppfølging');
INSERT INTO result (id, name, displaytext) VALUES (4, 'Passet ikke, må bli oppringt på spesifikt tidspunkt', 'Ba om å bli oppringt på spesifikt tidspunkt.');
INSERT INTO result (id, name, displaytext) VALUES (5, 'Vil bli valgkampfrivillig og aktiv i et lokallag', 'Vil bli valgkampfrivillig og aktiv i et lokallag');
INSERT INTO result (id, name, displaytext) VALUES (6, 'Vil bli valgkampfrivillig (henvis til rødt.no/frivillig, og si at de også får en e-post med info)', 'Vil bli valgkampfrivillig (henvis til rødt.no/frivillig, og si at de også får en e-post med info om hvordan man kan melde seg.)');
INSERT INTO result (id, name, displaytext) VALUES (7, 'Nei', 'Nei');
INSERT INTO result (id, name, displaytext) VALUES (8, 'Vil bli aktiv i et lokallag(Vi følger opp aktivt med nærmeste lokallag. Om de sier at det ikke finnes lokallag der de bor, fortell hvordan man kan melde seg som kontaktperson og starte lokallag).', 'Vil bli aktiv i et lokallag(Vi følger opp aktivt med nærmeste lokallag. Om de sier at det ikke finnes lokallag der de bor, fortell hvordan man kan melde seg som kontaktperson og starte lokallag).');
INSERT INTO result (id, name, displaytext, skalSkjules, svarte) VALUES (9, 'Samtale startet', 'Samtale startet', 1, 0);
INSERT INTO result (id, name, displaytext, svarte) VALUES (10, 'Flere enn to ikke-svar', 'Flere enn to ikke-svar', 0);
INSERT INTO result (id, name, displaytext) VALUES (11, 'Svarte', 'Svarte');

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `modusTilResultat` (
  `modus` int(2) NOT NULL,
  `resultat` int(2) NOT NULL,
  PRIMARY KEY (`modus`, `resultat`),
  INDEX (`resultat`),
  FOREIGN KEY (`resultat`) REFERENCES `result` (`id`),
  INDEX(`modus`),
  FOREIGN KEY (`modus`) REFERENCES `modus` (`id`)
);


-- Korona
INSERT INTO modusTilResultat VALUES ((select id from modus where name = 'korona'), 0);
INSERT INTO modusTilResultat VALUES ((select id from modus where name = 'korona'), 4);
INSERT INTO modusTilResultat VALUES ((select id from modus where name = 'korona'), 9);
INSERT INTO modusTilResultat VALUES ((select id from modus where name = 'korona'), 11);

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `godkjenning` (
  `id` int(1) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `godkjenner` int(6) NOT NULL,
  `godkjentPerson` varchar(15) NOT NULL,
  `nyGroupId` int(2) NOT NULL,
  `tidspunkt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX(`godkjenner`),
  FOREIGN KEY (`godkjenner`) REFERENCES `ringer` (`id`),
  INDEX(`godkjentPerson`),
  FOREIGN KEY (`godkjentPerson`) REFERENCES `person` (`phone`),
  INDEX(`nyGroupId`),
  FOREIGN KEY (`nyGroupId`) REFERENCES `callGroup` (`id`)
);

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `call` (
  `callID` int(11) unsigned NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `calledPhone` varchar(15) NOT NULL,
  `ringer` int(6) NOT NULL,
  `datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `result` int(1) NOT NULL DEFAULT '0',
  `comment` longtext,
  UNIQUE KEY `callID` (`callID`),
  KEY `callID_2` (`callID`),
  INDEX (`result`),
  FOREIGN KEY (`result`) REFERENCES `result` (`id`),
  INDEX(`calledPhone`),
  FOREIGN KEY (`calledPhone`) REFERENCES `person` (`phone`),
  INDEX(`ringer`),
  FOREIGN KEY (`ringer`) REFERENCES `ringer` (`id`)
);

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `login_attempts` (
  `hypersysID` int(6) NOT NULL,
  `datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`hypersysID`) REFERENCES `person` (`hypersysID`),
  INDEX(`hypersysID`)
);

-- --------------------------------------------------------

create or replace view v_resultatForModus as
SELECT r.id, r.name, m.id as modus, r.displaytext, r.skalSkjules
FROM `result` r
INNER JOIN `modusTilResultat` mr on mr.resultat = r.id
INNER JOIN `modus` m on mr.modus = m.id;

-- --------------------------------------------------------

create or replace view v_personerSomKanRinges as
SELECT p.lastCall, p.phone, concat(p.givenName,' ',p.familyName) as name, p.postnumber, p.countyID, p.lokallag, l.name as lokallagNavn, p.id as id
  FROM person p
  LEFT OUTER JOIN lokallag l on p.lokallag = l.id
  WHERE groupID = '1'
  AND UNIX_TIMESTAMP(now()) - lastCall > 86400; -- 86400 sekund = 1 døgn

-- --------------------------------------------------------

create or replace view v_callsResult AS
SELECT distinct concat(ringerPerson.givenName,' ',ringerPerson.familyName) as callerName, ringerPerson.phone as callerPhone, c.datetime as `datetime`, c.comment, r.displaytext as result, c.calledPhone, r.svarte, r.id as resultId
FROM `call` c
INNER JOIN `result`r on r.id = c.result
INNER JOIN `ringer` ringer on ringer.id = c.ringer
INNER join `person` ringerPerson on ringerPerson.id = ringer.personId
WHERE c.result != 9
ORDER BY c.datetime ASC;

-- --------------------------------------------------------

create or replace view v_igjenAaRinge AS
SELECT p.countyID, p.lokallag
FROM `person` p
WHERE p.groupID = 1 or p.groupID = 0;

-- --------------------------------------------------------

create or replace view v_totaltInklRingte AS
SELECT p.countyID, p.lokallag
FROM `person` p
WHERE p.groupID = 2;

-- --------------------------------------------------------

create or replace view v_noenRingerTilbake AS
SELECT concat(p.givenName,' ',p.familyName) as name, p.postnumber, p.phone, l.name as lokallagNavn, l.id as lokallag, ringer.id as ringer
FROM person p
inner join `call` c on p.phone = c.calledPhone
inner join `ringer` ringer on ringer.id = c.ringer
left outer join lokallag l on p.lokallag = l.id;

-- --------------------------------------------------------

create or replace view v_ringtFlest AS
select count(ringer.id) as max, person.lokallag from
`call` c
inner join ringer ringer on ringer.id = c.ringer and c.result != 9
inner join `person` person on person.id = ringer.personId
group by(ringer.id)
order by count(ringer.id) desc;

-- --------------------------------------------------------

create or replace view v_personerGodkjenning AS
SELECT r.userCreated, concat(givenName, ' ', familyName) as name, phone, l.id as lokallagId, l.name as lokallag, email, postnumber, p.groupID
FROM `person` p
inner join `ringer` r on p.id = r.personId
left outer join `lokallag` l on p.lokallag = l.id order by p.groupID asc, r.userCreated asc;

-- --------------------------------------------------------

DELIMITER //
  DROP PROCEDURE IF EXISTS sp_updateGroupID;
  CREATE PROCEDURE sp_updateGroupID(
  phone_In varchar(15),
  groupID_In int(2)
)
BEGIN
update `person` 
set groupID = groupID_In
where phone = phone_In;
END //

-- --------------------------------------------------------

DELIMITER //
  DROP PROCEDURE IF EXISTS sp_registrerSamtale;
  CREATE PROCEDURE sp_registrerSamtale(
    calledPhoneIn varchar(15),
    ringerIdIn varchar(15),
    resultIn int(1),
    commentIn longtext
)
BEGIN
INSERT INTO `call` (calledPhone, ringer, result, comment)
VALUES (calledPhoneIn, ringerIdIn, resultIn, commentIn);
UPDATE `person` 
  SET lastCall = UNIX_TIMESTAMP(now())
  WHERE phone = calledPhoneIn;
END //
-- --------------------------------------------------------

DELIMITER //
  DROP PROCEDURE IF EXISTS sp_godkjennBruker;
  CREATE PROCEDURE sp_godkjennBruker(
    ringerIdIn varchar(15),
    calledPhoneIn varchar(15),
    nyGroupIdIn int(2)
)
BEGIN
INSERT INTO `godkjenning` (godkjenner, godkjentPerson, nyGroupId) 
VALUES (ringerIdIn, calledPhoneIn, nyGroupIdIn);
UPDATE `person` 
  SET groupID = nyGroupIdIn
  WHERE phone = calledPhoneIn;
END //

-- --------------------------------------------------------

DELIMITER //
  DROP PROCEDURE IF EXISTS sp_registrerOppfoelgingKorona;
  CREATE PROCEDURE sp_registrerOppfoelgingKorona(
    calledPhoneIn varchar(15),
    koronaprogramIn tinyint(1),
    merAktivIn tinyint(1),
    valgkampsbrevIn tinyint(1),
    vilIkkeBliRingtIn tinyint(1)
)
BEGIN
INSERT INTO `oppfoelgingKorona` (personId, koronaprogram, merAktiv, valgkampsbrev, vilIkkeBliRingt)
  VALUES ((select id from person where phone = calledPhoneIn), koronaprogramIn, merAktivIn, valgkampsbrevIn, vilIkkeBliRingtIn);
END //

-- --------------------------------------------------------

DELIMITER //
  DROP PROCEDURE IF EXISTS sp_startSamtale;
  CREATE PROCEDURE sp_startSamtale(
    calledPhoneIn varchar(15),
    ringerIdIn varchar(15)
)
BEGIN
INSERT INTO `call` (calledPhone, ringer, result, comment)
VALUES (calledPhoneIn, ringerIdIn, '9', 'Starter samtale');
UPDATE `person` SET lastCall = UNIX_TIMESTAMP(now()) WHERE phone = calledPhoneIn;
END //

-- --------------------------------------------------------

DELIMITER //
  DROP PROCEDURE IF EXISTS sp_registrerNyBruker;
  CREATE PROCEDURE sp_registrerNyBruker(
    hypersysIDIn int(4),
    givenNameIn varchar(60),
    familyNameIn varchar(60),
    phoneIn varchar(15),
    emailIn varchar(100),
    postnumberIn int(4),
    countyIDIn tinyint(2),
    lokallagIn int(3)
)
BEGIN

  IF (SELECT count(1) FROM `person` where phone = phoneIn)>0 THEN
    BEGIN
      UPDATE `person` SET
          hypersysID = hypersysIDIn,
          givenName = givenNameIn, 
          familyName = familyNameIn,
          email = emailIn, 
          postnumber = postnumberIn, 
          groupID = greatest(4, groupID),
          countyID = countyIDIn,
          lokallag = lokallagIn
        WHERE phone = phoneIn;
    END;
  ELSE
    BEGIN
        INSERT INTO `person` (hypersysID, givenName, familyName, phone, email, postnumber, countyID, groupID, lokallag)
            VALUES (hypersysIDIn, givenNameIn, familyNameIn, phoneIn, emailIn, postnumberIn, countyIDIn, '4', lokallagIn);
    END;
  END IF;

  IF (SELECT count(1) FROM `person` p inner join `ringer` r on p.id = r.personId where p.email = emailIn and r.id is not null)>0 THEN
    BEGIN
      SET @personId =(select `id` FROM `person` where email = emailIn);
    END;
  ELSE
    BEGIN
      SET @personId =(SELECT last_insert_id());
      INSERT INTO `ringer` (`personId`) VALUES(@personId);
    END;

  END IF;

END //

-- --------------------------------------------------------

DELIMITER //
  DROP PROCEDURE IF EXISTS sp_slettPerson;
  CREATE PROCEDURE sp_slettPerson (
    phoneIn varchar(15)
)
BEGIN
DELETE FROM `call` where calledPhone = phoneIn;
DELETE FROM `oppfoelgingKorona` where personId = (select id from person where phone = phoneIn);
DELETE FROM `ringer` where personId = (select id from person where phone = phoneIn);
DELETE FROM `person` where phone = phoneIn;
END //

-- --------------------------------------------------------

DELIMITER //
  DROP PROCEDURE IF EXISTS sp_recordLoginAttempt;
  CREATE PROCEDURE sp_recordLoginAttempt (
    hypersysIDIn int(6)
  )
BEGIN
INSERT INTO `login_attempts` (hypersysID)
VALUES
  (hypersysIDIn);
END //


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;