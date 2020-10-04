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
(50, 'Trøndelag');


CREATE TABLE IF NOT EXISTS `postnumber` (
  `postnumber` varchar(4) NOT NULL,
  `postplace` varchar(250) NOT NULL,
  `countyID` int(2) NOT NULL,
  `municipalityID` int(2) NOT NULL,
  `municipality` varchar(30) NOT NULL,
  PRIMARY KEY (`postnumber`),
  INDEX(`countyID`),
  FOREIGN KEY (`countyID`) REFERENCES `fylker`(`id`)
);

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `modus` (
  `id` int(2) NOT NULL PRIMARY KEY,
  `name` varchar(60) NOT NULL
);

INSERT INTO modus VALUES (0, 'ekstern');
INSERT INTO modus VALUES (1, 'intern');
INSERT INTO modus VALUES (2, 'korona');


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

CREATE TABLE IF NOT EXISTS `ringer` (
  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `password` varchar(128) NOT NULL,
  `userCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(`id`)
);

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `lokallag` (
  `id` int(3) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL UNIQUE,
  PRIMARY KEY(`id`)
);

insert into `lokallag` (name) values 
  ('Rødt Grorud'),
  ('Rødt Bergen Nord'),
  ('Rødt Bergen Studentlag'),
  ('Rødt Bergen Sør'),
  ('Rødt Bergen Sentrum'),
  ('Rødt Bergen Vest'),
  ('Rødt Oslo Østensjø'),
  ('Rødt Skien'),
  ('Rødt Tromsø'),
  ('Rødt Ålesund'),
  ('Rødt Notodden');

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `person` (
  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `givenName` varchar(60) DEFAULT NULL,
  `familyName` varchar(60) DEFAULT NULL,
  `phone` varchar(15) NOT NULL UNIQUE,
  `nameEnlister` varchar(30) DEFAULT NULL,
  `adressLine1` mediumtext,
  `adressLine2` mediumtext,
  `postnumber` varchar(4) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `countyID` int(2) DEFAULT -1 NOT NULL,
  `groupID` int(2) DEFAULT NULL,
  `hasReceived` int(1) DEFAULT NULL,
  `hasConsented` tinyint(1) DEFAULT NULL,
  `isDigital` tinyint(1) NOT NULL DEFAULT 0,
  `userCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastCall` int(11) NOT NULL DEFAULT '0',
  `ringerID`int(6) unsigned DEFAULT NULL,
  `lokallag` int(3) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`groupID`) REFERENCES `callGroup` (`id`),
  FOREIGN KEY(`countyID`) REFERENCES `fylker` (`id`),
  FOREIGN KEY(`ringerID`) REFERENCES `ringer` (`id`),
  FOREIGN KEY(`lokallag`) REFERENCES `lokallag` (`id`),
  FOREIGN KEY(`postnumber`) REFERENCES `postnumber` (`postnumber`),
  INDEX (`groupID`),
  INDEX (`countyID`),
  INDEX (`phone`),
  INDEX (`lokallag`),
  INDEX (`postnumber`)
);

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `oppfoelgingKorona` (
  `personId` int(6) unsigned NOT NULL,
  `koronaprogram` tinyint(1) DEFAULT NULL,
  `merAktiv` tinyint(1) DEFAULT NULL,
  `valgkampsbrev` tinyint(1) DEFAULT NULL,
  `vilIkkeBliRingt` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`personId`),
  FOREIGN KEY (`personId`) REFERENCES `person` (`id`),
  INDEX (`personId`)
);

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `oppfoelgingEkstern` (
  `personId` int(6) unsigned NOT NULL,
  `hasActuallyReceived` tinyint(1) DEFAULT NULL,
  `stopSubscription` tinyint(1) DEFAULT NULL,
  `smsReminder` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`personId`),
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

-- Ekstern
INSERT INTO modusTilResultat VALUES (0, 0);
INSERT INTO modusTilResultat VALUES (0, 1);
INSERT INTO modusTilResultat VALUES (0, 2);
INSERT INTO modusTilResultat VALUES (0, 3);
INSERT INTO modusTilResultat VALUES (0, 4);
INSERT INTO modusTilResultat VALUES (0, 5);
INSERT INTO modusTilResultat VALUES (0, 9);

-- Intern
INSERT INTO modusTilResultat VALUES (1, 0);
INSERT INTO modusTilResultat VALUES (1, 6);
INSERT INTO modusTilResultat VALUES (1, 7);
INSERT INTO modusTilResultat VALUES (1, 8);
INSERT INTO modusTilResultat VALUES (1, 9);
INSERT INTO modusTilResultat VALUES (1, 4);
INSERT INTO modusTilResultat VALUES (1, 10);

-- Korona
INSERT INTO modusTilResultat VALUES (2, 0);
INSERT INTO modusTilResultat VALUES (2, 4);
INSERT INTO modusTilResultat VALUES (2, 9);
INSERT INTO modusTilResultat VALUES (2, 11);

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `typeCall` (
  `id` int(1) NOT NULL PRIMARY KEY,
  `name` varchar(20) NOT NULL 
);

INSERT INTO typeCall VALUES(1, 'ringt');
INSERT INTO typeCall VALUES(2, 'SMS');
INSERT INTO typeCall VALUES(3, 'e-post');

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `godkjenning` (
  `id` int(1) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `godkjenner` varchar(15) NOT NULL,
  `godkjentPerson` varchar(15) NOT NULL,
  `nyGroupId` int(2) NOT NULL,
  `tidspunkt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX(`godkjenner`),
  FOREIGN KEY (`godkjenner`) REFERENCES `person` (`phone`),
  INDEX(`godkjentPerson`),
  FOREIGN KEY (`godkjentPerson`) REFERENCES `person` (`phone`),
  INDEX(`nyGroupId`),
  FOREIGN KEY (`nyGroupId`) REFERENCES `callGroup` (`id`)
);

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `call` (
  `callID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `calledPhone` varchar(15) NOT NULL,
  `callerPhone` varchar(8) NOT NULL,
  `datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `typeCall` int(1) DEFAULT NULL,
  `result` int(1) NOT NULL DEFAULT '0',
  `comment` longtext,
  PRIMARY KEY (`callID`),
  UNIQUE KEY `callID` (`callID`),
  KEY `callID_2` (`callID`),
  INDEX (`result`),
  FOREIGN KEY (`result`) REFERENCES `result` (`id`),
  INDEX(`typeCall`),
  FOREIGN KEY (`typeCall`) REFERENCES `typeCall` (`id`),
  INDEX(`calledPhone`),
  FOREIGN KEY (`calledPhone`) REFERENCES `person` (`phone`),
  INDEX(`callerPhone`),
  FOREIGN KEY (`callerPhone`) REFERENCES `person` (`phone`)
);

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `login_attempts` (
  `user_id` int(6) NOT NULL,
  `time` varchar(30) NOT NULL
);

-- --------------------------------------------------------

create or replace view v_resultatForModus as
SELECT r.id, r.name, m.id as modus, r.displaytext, r.skalSkjules
FROM `result` r
INNER JOIN `modusTilResultat` mr on mr.resultat = r.id
INNER JOIN `modus` m on mr.modus = m.id;

-- --------------------------------------------------------

create or replace view v_navnOgAdresse as
SELECT concat(p.givenName,' ',p.familyName) as name, p.adressLine1, p.adressLine2, p.postnumber 
FROM person p 
WHERE p.groupID <= 1 AND p.adressLine1 IS NOT NULL AND p.adressLine1 != '' AND p.hasReceived IS NULL;

-- --------------------------------------------------------

create or replace view v_soekForSMSsamtale as
SELECT id, phone, email, isDigital, nameEnlister, concat(p.givenName,' ',p.familyName) as name, postnumber, adressLine1, adressLine2 
FROM person  p
WHERE groupID = 0;

-- --------------------------------------------------------

create or replace view v_personerSomKanRinges as
SELECT p.nameEnlister, p.lastCall, p.phone, concat(p.givenName,' ',p.familyName) as name, p.postnumber, p.countyID, p.lokallag, l.name as lokallagNavn, p.id as id
  FROM person p
  LEFT OUTER JOIN lokallag l on p.lokallag = l.id
  WHERE groupID = '1'
  AND UNIX_TIMESTAMP(now()) - lastCall > 86400; -- 86400 sekund = 1 døgn

-- --------------------------------------------------------

create or replace view v_fysiskIkkeSamtykketIkkeAdresse as
SELECT id, phone, givenName, familyName
        FROM person p
        WHERE groupID = 0
          AND isDigital = 0
          AND hasReceived IS NULL
          AND hasConsented IS NULL;

-- --------------------------------------------------------

create or replace view v_faarRoedtNytt as
SELECT hasReceived, isDigital, phone FROM person;

-- --------------------------------------------------------

create or replace view v_numbersForSendDigital as
SELECT email, id, phone, p.givenName, p.familyName
FROM person p
WHERE CHAR_LENGTH(phone) = 8
  AND groupID <= 1
  AND isDigital = 1
  AND hasReceived IS NULL
  AND email != '';

-- --------------------------------------------------------

create or replace view v_callsResult AS
SELECT distinct concat(caller.givenName,' ',caller.familyName) as callerName, caller.phone as callerPhone, c.datetime as `datetime`, c.comment, r.displaytext as result, c.calledPhone, r.svarte, r.id as resultId
FROM `call` c
INNER JOIN `result`r on r.id = c.result
INNER JOIN `person` caller on caller.phone = c.callerPhone
WHERE typeCall = 1
AND c.result != 9
ORDER BY c.datetime ASC;

-- --------------------------------------------------------

create or replace view v_igjenAaRinge AS
SELECT p.countyID, p.lokallag
FROM `person` p
WHERE p.groupID = 1 or p.groupID = 0;

-- --------------------------------------------------------

create or replace view v_kanRingesNaa AS
SELECT p.countyID
FROM `person` p
WHERE p.groupID = 1;

-- --------------------------------------------------------

create or replace view v_ringer AS
SELECT p.phone, l.name as lokallag, concat(p.givenName,' ',p.familyName) as name
FROM `person` p
left outer join lokallag l on p.lokallag = l.id;

-- --------------------------------------------------------

create or replace view v_totaltInklRingte AS
SELECT p.countyID, p.lokallag
FROM `person` p
WHERE p.groupID = 2;

-- --------------------------------------------------------

create or replace view v_noenRingerTilbake AS
SELECT p.nameEnlister, concat(p.givenName,' ',p.familyName) as name, p.postnumber, p.phone, l.name as lokallagNavn, l.id as lokallag
FROM person p
left outer join lokallag l on p.lokallag = l.id;

-- --------------------------------------------------------

create or replace view v_caller AS
SELECT p.postnumber, concat(p.givenName,' ',p.familyName) as name, p.countyID, p.phone, p.lokallag as lokallagID, l.name as lokallag
FROM `person` p left outer join lokallag l on p.lokallag = l.id;

-- --------------------------------------------------------

create or replace view v_ringtFlest AS
select count(c.callerPhone) as max, p.lokallag from
`call` c
inner join person p on c.callerPhone = p.phone and c.result != 9 and c.typeCall = 1
group by(c.callerPhone)
order by count(c.callerPhone) desc;

-- --------------------------------------------------------


create or replace view v_ringerForInnlogging AS
select distinct r.id, p.phone, r.password, p.email
from `person` p
inner join `ringer` r on p.ringerID = r.id;

-- --------------------------------------------------------

create or replace view v_personerGodkjenning AS
SELECT userCreated, concat(givenName, ' ', familyName) as name, phone, l.id as lokallagId, l.name as lokallag, email, postnumber, p.groupID
FROM `person` p 
left outer join `lokallag` l on p.lokallag = l.id;

-- --------------------------------------------------------

create or replace view v_hvorMangeRingt AS
SELECT p.lokallag as lokallag, c.callerPhone, c.typeCall, c.result
FROM 
`call` c
inner join `person` p on c.callerPhone = p.phone;

-- --------------------------------------------------------

DELIMITER //
  CREATE OR REPLACE PROCEDURE sp_updateGroupID(
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
  CREATE OR REPLACE PROCEDURE sp_registrerSamtale(
    calledPhoneIn varchar(15),
    callerPhoneIn varchar(15),
    resultIn int(1),
    commentIn longtext
)
BEGIN
INSERT INTO `call` (calledPhone, callerPhone, typeCall, result, comment) 
VALUES (calledPhoneIn, callerPhoneIn, '1', resultIn, commentIn);
UPDATE `person` 
  SET lastCall = UNIX_TIMESTAMP(now())
  WHERE phone = calledPhoneIn;
END //
-- --------------------------------------------------------

DELIMITER //
  CREATE OR REPLACE PROCEDURE sp_godkjennBruker(
    callerPhoneIn varchar(15),
    calledPhoneIn varchar(15),
    nyGroupIdIn int(2)
)
BEGIN
INSERT INTO `godkjenning` (godkjenner, godkjentPerson, nyGroupId) 
VALUES (callerPhoneIn, calledPhoneIn, nyGroupIdIn);
UPDATE `person` 
  SET groupID = nyGroupIdIn
  WHERE phone = calledPhoneIn;
END //

-- --------------------------------------------------------

DELIMITER //
  CREATE OR REPLACE PROCEDURE sp_registrerOppfoelgingKorona(
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
  CREATE OR REPLACE PROCEDURE sp_registrerOppfoelgingEkstern(
    calledPhoneIn varchar(15),
    hasActuallyReceivedIn tinyint(1),
    stopSubscriptionIn tinyint(1),
    smsReminderIn tinyint(1)
)
BEGIN
INSERT INTO `oppfoelgingEkstern` (personId, hasActuallyReceived, stopSubscription, smsReminder)
  VALUES ((select id from person where phone = calledPhoneIn), hasActuallyReceiveIn, stopSubscriptionIn, smsReminderIn);
END //

-- --------------------------------------------------------

DELIMITER //
  CREATE OR REPLACE PROCEDURE sp_startSamtale(
    calledPhoneIn varchar(15),
    callerPhoneIn varchar(15)
)
BEGIN
INSERT INTO `call` (calledPhone, callerPhone, typeCall, result, comment) 
VALUES (calledPhoneIn, callerPhoneIn, '1', '9', 'Starter samtale');
UPDATE `person` SET lastCall = UNIX_TIMESTAMP(now()) WHERE phone = calledPhoneIn;
END //

-- --------------------------------------------------------

DELIMITER //
  CREATE OR REPLACE PROCEDURE sp_enlistAddress(
    givenNameIn varchar(60),
    familyNameIn varchar(60),
    calledPhoneIn varchar(15),
    calledAddressIn mediumtext,
    calledAddressIn2 mediumtext,
    calledPostnumberIn int(4),
    calledCountyIn tinyint(2),
    nameEnlisterIn varchar(30),
    hasConsentedIn tinyint(1)
)
BEGIN
INSERT INTO `person` (givenName, familyName, phone, adressLine1, adressLine2, postnumber, countyID, nameEnlister, groupID) 
VALUES (givenNameIn, familyNameIn, calledPhoneIn, calledAddressIn, calledAddressIn2, calledPostnumberIn, calledCountyIn,
 nameEnlisterIn, hasConsentedIn);   
END //

-- --------------------------------------------------------

DELIMITER //
  CREATE OR REPLACE PROCEDURE sp_registrerNyBruker(
    givenNameIn varchar(60),
    familyNameIn varchar(60),
    phoneIn varchar(15),
    emailIn varchar(100),
    postnumberIn int(4),
    countyIDIn tinyint(2),
    passwordIn varchar(128)
)
BEGIN
INSERT INTO `ringer` (password) VALUES(passwordIn);

  IF (SELECT count(1) FROM `person` where phone = phoneIn)>0 THEN
    BEGIN
      UPDATE `person` SET 
          givenName = givenNameIn, 
          familyName = familyNameIn,
          email = emailIn, 
          postnumber = postnumberIn, 
          groupID = '4',
          countyID = countyIDIn,
          ringerID = (SELECT last_insert_id())
        WHERE phone = phoneIn and groupID < 4;
    END;
  ELSE
    BEGIN
        INSERT INTO `person` (givenName, familyName, phone, email, postnumber, countyID, groupID, ringerID)
            VALUES (givenNameIn, familyNameIn, phoneIn, emailIn, postnumberIn, countyIDIn, '4', (SELECT last_insert_id()));
    END;
  END IF;
END //

-- --------------------------------------------------------

DELIMITER //
  CREATE OR REPLACE PROCEDURE sp_vervEnVenn (
    nameIn varchar(60),
    phoneIn varchar(15),
    phoneEnlisterIn varchar(15),
    nameEnlisterIn varchar(60)
)
BEGIN
INSERT INTO `person` (name, phone, phoneEnlister, nameEnlister, groupID) 
        VALUES (nameIn, phoneIn, phoneEnlisterIn, nameEnlisterIn, '0');        
END //

-- --------------------------------------------------------

DELIMITER //
  CREATE OR REPLACE PROCEDURE sp_vervEnVennProcessEnlist (
    nameIn varchar(60),
    phoneIn varchar(15),
    nameEnlisterIn varchar(60),
    isDigital tinyint(1)
)
BEGIN
INSERT INTO `person` (name, phone, nameEnlister, groupID, isDigital, countyID) 
        VALUES (nameIn, phoneIn, nameEnlisterIn, '0', isDigital, -1);
END //

-- --------------------------------------------------------

DELIMITER //
  CREATE OR REPLACE PROCEDURE sp_deletePersonAfterSMS (
    addressIn mediumtext,
    addressIn2 mediumtext,
    postnumberIn int(4),
    countyIn tinyint(2),
    phoneIn varchar(15)
)
BEGIN
UPDATE `person` 
  SET
    adressLine1 = addressIn, 
    adressLine2 = addressIn2, 
    postnumber = postnumberIn, 
    countyID = countyIn, 
    groupID = '3' 
    WHERE phone = phoneIn;
END //

-- --------------------------------------------------------

DELIMITER //
  CREATE OR REPLACE PROCEDURE sp_slettPerson (
    phoneIn varchar(15)
)
BEGIN
DELETE FROM `call` where calledPhone = phoneIn;
DELETE FROM `oppfoelgingEkstern` where personId = (select id from person where phone = phoneIn);
DELETE FROM `oppfoelgingKorona` where personId = (select id from person where phone = phoneIn);
DELETE FROM `ringer` where id = (select ringerID from person where phone = phoneIn);
DELETE FROM `person` where phone = phoneIn;
END //

-- --------------------------------------------------------

DELIMITER //
  CREATE OR REPLACE PROCEDURE sp_registerSMS (
    addressIn mediumtext,
    addressIn2 mediumtext,
    emailIn varchar(100),
    postnumberIn int(4),
    countyIn tinyint(2),
    calledPhoneIn varchar(15),
    commentIn longtext,
    callerPhoneIn varchar(15) 
)
BEGIN
UPDATE `person` 
  SET
    adressLine1 = addressIn, 
    adressLine2 = addressIn2, 
    email = emailIn,
    postnumber = postnumberIn, 
    countyID = countyIn
    WHERE phone = calledPhoneIn;
UPDATE `call` 
  SET 
    result = 1, 
    comment = commentIn 
  WHERE 
  calledPhone = calledPhoneIn AND 
  callerPhone = callerPhoneIn AND 
  typeCall = 2;
END //

-- --------------------------------------------------------

DELIMITER //
  CREATE OR REPLACE PROCEDURE sp_updateAfterSendDigital (
    descReceivingIn mediumtext
)
BEGIN
UPDATE `person` 
  SET 
    groupID = 1, 
    hasReceived = 2, 
    descReceiving = descReceivingIn
  WHERE 
    CHAR_LENGTH(phone) = 8 AND 
    groupID <= 1 AND 
    isDigital = 1 AND 
    hasReceived IS NULL AND 
    email != '';
END //

-- --------------------------------------------------------

DELIMITER //
  CREATE OR REPLACE PROCEDURE sp_updateAfterSendAdress ()
BEGIN
UPDATE `person` 
  SET 
    groupID = 1, 
    hasReceived = 2
  WHERE 
    groupID <= 1 AND
    adressLine1 IS NOT NULL AND 
    adressLine1 != '' AND
    hasReceived IS NULL;
END //

-- --------------------------------------------------------

DELIMITER //
  CREATE OR REPLACE PROCEDURE sp_lagreAtSMSErSendt (
    calledPhoneIn varchar(15),
    callerPhoneIn varchar(15)
  )
BEGIN
INSERT INTO `call` (calledPhone, callerPhone, typeCall)
VALUES
  (calledPhoneIn, callerPhoneIn, 2);
END //

-- --------------------------------------------------------

DELIMITER //
  CREATE OR REPLACE PROCEDURE sp_recordLoginAttempt (
    userId_in int(6)
  )
BEGIN
INSERT INTO `login_attempts` (user_id, time)
VALUES
  (userId_in, UNIX_TIMESTAMP(now()));
END //


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;