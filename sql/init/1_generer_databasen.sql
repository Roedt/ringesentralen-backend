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

CREATE TABLE IF NOT EXISTS `kommune` (
  `nummer` integer NOT NULL PRIMARY KEY,
  `navn` varchar(50) NOT NULL,
  `lokallag_id` int(3) unsigned DEFAULT NULL,
  `fylke_id` int(2) NOT NULL,
  FOREIGN KEY (`lokallag_id`) REFERENCES `lokallag` (`id`),
  INDEX(`lokallag_id`),
  FOREIGN KEY (`fylke_id`) REFERENCES `fylker` (`id`),
  INDEX(`fylke_id`)
);

    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Aremark',118,1, (select id from lokallag where name = 'Rødt Indre Østfold'));
    INSERT INTO kommune(navn,nummer,fylke_id, lokallag_id) VALUES ('Askim',124,1, (select id from lokallag where name = 'Rødt Indre Østfold'));
    INSERT INTO kommune(navn,nummer,fylke_id, lokallag_id) VALUES ('Eidsberg',125,1, (select id from lokallag where name = 'Rødt Indre Østfold'));
    INSERT INTO kommune(navn,nummer,fylke_id, lokallag_id) VALUES ('Fredrikstad',106,1, (select id from lokallag where name = 'Rødt Fredrikstad'));
    INSERT INTO kommune(navn,nummer,fylke_id, lokallag_id) VALUES ('Halden',101,1, (select id from lokallag where name = 'Rødt Halden'));
    INSERT INTO kommune(navn,nummer,fylke_id, lokallag_id) VALUES ('Hobøl',138,1, (select id from lokallag where name = 'Rødt Indre Østfold'));
    INSERT INTO kommune(navn,nummer,fylke_id, lokallag_id) VALUES ('Hvaler',111,1, (select id from lokallag where name = 'Rødt Fredrikstad'));
    INSERT INTO kommune(navn,nummer,fylke_id, lokallag_id) VALUES ('Marker',119,1, (select id from lokallag where name = 'Rødt Indre Østfold'));
    INSERT INTO kommune(navn,nummer,fylke_id, lokallag_id) VALUES ('Moss',104,1, (select id from lokallag where name = 'Rødt Moss og omegn'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Råde',135,1, (select id from lokallag where name = 'Rødt Råde'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Rakkestad',128,1, (select id from lokallag where name = 'Rødt Sarpsborg og omegn'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Rømskog',121,1, (select id from lokallag where name = 'Rødt Aurskog-Høland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Rygge',136,1, (select id from lokallag where name = 'Rødt Moss og omegn'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sarpsborg',105,1, (select id from lokallag where name = 'Rødt Sarpsborg og omegn'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Skiptvet',127,1, (select id from lokallag where name = 'Rødt Indre Østfold'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Spydeberg',123,1, (select id from lokallag where name = 'Rødt Indre Østfold'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Trøgstad',122,1, (select id from lokallag where name = 'Rødt Indre Østfold'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Våler i Østfold',137,1, (select id from lokallag where name = 'Rødt Våler i Østfold'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Ås',214,2, (select id from lokallag where name = 'Rødt Ås'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Asker',220,2, (select id from lokallag where name = 'Rødt Asker'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Aurskog-Høland',221,2, (select id from lokallag where name = 'Rødt Aurskog-Høland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Bærum',219,2, (select id from lokallag where name = 'Rødt Bærum'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Eidsvoll',237,2, (select id from lokallag where name = 'Rødt Eidsvoll og Hurdal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Enebakk',229,2, (select id from lokallag where name = 'Rødt Nordre Follo'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Fet',227,2, (select id from lokallag where name = 'Rødt Lillestrøm'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Frogn',215,2, (select id from lokallag where name = 'Rødt Frogn'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Gjerdrum',234,2, (select id from lokallag where name = 'Rødt Lillestrøm'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hurdal',239,2, (select id from lokallag where name = 'Rødt Eidsvoll og Hurdal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Lørenskog',230,2, (select id from lokallag where name = 'Rødt Lørenskog'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Nannestad',238,2, (select id from lokallag where name = 'Rødt Ullensaker og Nannestad'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Nes i Akershus',236,2, (select id from lokallag where name = 'Rødt Nes'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Nesodden',216,2, (select id from lokallag where name = 'Rødt Nesodden'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Nittedal',233,2, (select id from lokallag where name = 'Rødt Nittedal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Oppegård',217,2, (select id from lokallag where name = 'Rødt Nordre Follo'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Rælingen',228,2, (select id from lokallag where name = 'Rødt Lillestrøm'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Skedsmo',231,2, (select id from lokallag where name = 'Rødt Lillestrøm'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Ski',213,2, (select id from lokallag where name = 'Rødt Nordre Follo'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sørum',226,2, (select id from lokallag where name = 'Rødt Lillestrøm'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Ullensaker',235,2, (select id from lokallag where name = 'Rødt Ullensaker og Nannestad'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vestby',211,2, (select id from lokallag where name = 'Rødt Vestby'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Oslo',301,3, (select id from lokallag where name = 'Rødt Oslo'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Alvdal',438,4, (select id from lokallag where name = 'Rødt Folldal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Åmot',429,4, (select id from lokallag where name = 'Uten lag Hedmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Åsnes',425,4, (select id from lokallag where name = 'Rødt Åsnes'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Eidskog',420,4, (select id from lokallag where name = 'Rødt Kongsvinger og omegn'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Elverum',427,4, (select id from lokallag where name = 'Rødt Elverum'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Engerdal',434,4, (select id from lokallag where name = 'Uten lag Hedmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Folldal',439,4, (select id from lokallag where name = 'Rødt Folldal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Grue',423,4, (select id from lokallag where name = 'Rødt Kongsvinger og omegn'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hamar',403,4, (select id from lokallag where name = 'Rødt Hamar'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Kongsvinger',402,4, (select id from lokallag where name = 'Rødt Kongsvinger og omegn'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Løten',415,4, (select id from lokallag where name = 'Rødt Løten'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Nord-Odal',418,4, (select id from lokallag where name = 'Rødt Odal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Os i Hedmark',441,4, (select id from lokallag where name = 'Uten lag Hedmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Rendalen',432,4, (select id from lokallag where name = 'Uten lag Hedmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Ringsaker',412,4, (select id from lokallag where name = 'Rødt Ringsaker'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sør-Odal',419,4, (select id from lokallag where name = 'Rødt Odal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Stange',417,4, (select id from lokallag where name = 'Rødt Stange'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Stor-Elvdal',430,4, (select id from lokallag where name = 'Uten lag Hedmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Tolga',436,4, (select id from lokallag where name = 'Uten lag Hedmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Trysil',428,4, (select id from lokallag where name = 'Uten lag Hedmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Tynset',437,4, (select id from lokallag where name = 'Uten lag Hedmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Våler i Hedmark',426,4, (select id from lokallag where name = 'Rødt Kongsvinger og omegn'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Dovre',511,5, (select id from lokallag where name = 'Uten lag Oppland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Etnedal',541,5, (select id from lokallag where name = 'Raudt Valdres'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Gausdal',522,5, (select id from lokallag where name = 'Uten lag Oppland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Gjøvik',502,5, (select id from lokallag where name = 'Rødt Gjøvik og Land'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Gran',534,5, (select id from lokallag where name = 'Rødt Gran'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Jevnaker',532,5, (select id from lokallag where name = 'Rødt Ringerike'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Lesja',512,5, (select id from lokallag where name = 'Uten lag Oppland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Lillehammer',501,5, (select id from lokallag where name = 'Rødt Lillehammer'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Lom',514,5, (select id from lokallag where name = 'Uten lag Oppland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Lunner',533,5, (select id from lokallag where name = 'Rødt Lunner'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Nord-Aurdal',542,5, (select id from lokallag where name = 'Raudt Valdres'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Nord-Fron',516,5, (select id from lokallag where name = 'Rødt Midt-Gudbrandsdalen'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Nordre Land',538,5, (select id from lokallag where name = 'Rødt Gjøvik og Land'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Østre Toten',528,5, (select id from lokallag where name = 'Rødt Østre Toten'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Øyer',521,5, (select id from lokallag where name = 'Rødt Lillehammer'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Øystre Slidre',544,5, (select id from lokallag where name = 'Raudt Valdres'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Ringebu',520,5, (select id from lokallag where name = 'Rødt Midt-Gudbrandsdalen'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sel',517,5, (select id from lokallag where name = 'Uten lag Oppland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Skjåk',513,5, (select id from lokallag where name = 'Uten lag Oppland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Søndre Land',536,5, (select id from lokallag where name = 'Rødt Gjøvik og Land'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sør-Aurdal',540,5, (select id from lokallag where name = 'Raudt Valdres'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sør-Fron',519,5, (select id from lokallag where name = 'Rødt Midt-Gudbrandsdalen'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vågå',515,5, (select id from lokallag where name = 'Uten lag Oppland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vang',545,5, (select id from lokallag where name = 'Raudt Valdres'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vestre Slidre',543,5, (select id from lokallag where name = 'Raudt Valdres'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vestre Toten',529,5, (select id from lokallag where name = 'Rødt Vestre Toten'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Ål',619,6, (select id from lokallag where name = 'Rødt Ringerike'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Drammen',602,6, (select id from lokallag where name = 'Rødt Drammen'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Flå',615,6, (select id from lokallag where name = 'Rødt Ringerike'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Flesberg',631,6, (select id from lokallag where name = 'Rødt Kongsberg'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Gol',617,6, (select id from lokallag where name = 'Rødt Ringerike'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hemsedal',618,6, (select id from lokallag where name = 'Rødt Ringerike'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hol',620,6, (select id from lokallag where name = 'Rødt Ringerike'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hole',612,6, (select id from lokallag where name = 'Rødt Ringerike'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hurum',628,6, (select id from lokallag where name = 'Rødt Asker'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Kongsberg',604,6, (select id from lokallag where name = 'Rødt Kongsberg'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Krødsherad',622,6, (select id from lokallag where name = 'Rødt Ringerike'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Lier',626,6, (select id from lokallag where name = 'Rødt Lier'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Modum',623,6, (select id from lokallag where name = 'Rødt Modum'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Nedre Eiker',625,6, (select id from lokallag where name = 'Rødt Drammen'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Nes i Buskerud',616,6, (select id from lokallag where name = 'Rødt Ringerike'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Nore og Uvdal',633,6, (select id from lokallag where name = 'Rødt Kongsberg'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Øvre Eiker',624,6, (select id from lokallag where name = 'Rødt Øvre Eiker'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Ringerike',605,6, (select id from lokallag where name = 'Rødt Ringerike'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Rollag',632,6, (select id from lokallag where name = 'Rødt Kongsberg'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Røyken',627,6, (select id from lokallag where name = 'Rødt Asker'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sigdal',621,6, (select id from lokallag where name = 'Rødt Ringerike'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Færder',729,7, (select id from lokallag where name = 'Rødt Tønsberg og Færder'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Holmestrand',715,7, (select id from lokallag where name = 'Rødt Holmestrand'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Horten',701,7, (select id from lokallag where name = 'Rødt Horten'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Larvik',712,7, (select id from lokallag where name = 'Rødt Larvik'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Re',716,7, (select id from lokallag where name = 'Rødt Tønsberg og Færder'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sande i Vestfold',713,7, (select id from lokallag where name = 'Rødt Holmestrand'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sandefjord',710,7, (select id from lokallag where name = 'Rødt Sandefjord'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Svelvik',711,7, (select id from lokallag where name = 'Rødt Drammen'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Tønsberg',704,7, (select id from lokallag where name = 'Rødt Tønsberg og Færder'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Bamble',814,8, (select id from lokallag where name = 'Rødt Bamble'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Bø i Telemark',821,8, (select id from lokallag where name = 'Rødt Midt-Telemark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Drangedal',817,8, (select id from lokallag where name = 'Uten lag Telemark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Fyresdal',831,8, (select id from lokallag where name = 'Uten lag Telemark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hjartdal',827,8, (select id from lokallag where name = 'Rødt Notodden'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Kragerø',815,8, (select id from lokallag where name = 'Rødt Kragerø'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Kviteseid',829,8, (select id from lokallag where name = 'Uten lag Telemark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Nissedal',830,8, (select id from lokallag where name = 'Uten lag Telemark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Nome',819,8, (select id from lokallag where name = 'Rødt Midt-Telemark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Notodden',807,8, (select id from lokallag where name = 'Rødt Notodden'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Porsgrunn',805,8, (select id from lokallag where name = 'Rødt Porsgrunn'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sauherad',822,8, (select id from lokallag where name = 'Rødt Midt-Telemark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Seljord',828,8, (select id from lokallag where name = 'Rødt Notodden'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Siljan',811,8, (select id from lokallag where name = 'Uten lag Telemark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Skien',806,8, (select id from lokallag where name = 'Rødt Skien'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Tinn',826,8, (select id from lokallag where name = 'Rødt Notodden'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Tokke',833,8, (select id from lokallag where name = 'Uten lag Telemark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vinje',834,8, (select id from lokallag where name = 'Uten lag Telemark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Åmli',929,9, (select id from lokallag where name = 'Rødt Åmli'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Arendal',906,9, (select id from lokallag where name = 'Rødt Arendal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Birkenes',928,9, (select id from lokallag where name = 'Rødt Lillesand'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Bygland',938,9, (select id from lokallag where name = 'Rødt Setesdal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Bykle',941,9, (select id from lokallag where name = 'Rødt Setesdal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Evje og Hornnes',937,9, (select id from lokallag where name = 'Rødt Setesdal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Froland',919,9, (select id from lokallag where name = 'Rødt Froland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Gjerstad',911,9, (select id from lokallag where name = 'Rødt Risør'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Grimstad',904,9, (select id from lokallag where name = 'Rødt Grimstad'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Iveland',935,9, (select id from lokallag where name = 'Rødt Vennesla'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Lillesand',926,9, (select id from lokallag where name = 'Rødt Lillesand'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Risør',901,9, (select id from lokallag where name = 'Rødt Risør'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Tvedestrand',914,9, (select id from lokallag where name = 'Rødt Tvedestrand'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Valle',940,9, (select id from lokallag where name = 'Rødt Setesdal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vegårshei',912,9, (select id from lokallag where name = 'Rødt Vegårshei'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Åseral',1026,10, (select id from lokallag where name = 'Rødt Setesdal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Audnedal',1027,10, (select id from lokallag where name = 'Rødt Lyngdal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Farsund',1003,10, (select id from lokallag where name = 'Rødt Farsund'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Flekkefjord',1004,10, (select id from lokallag where name = 'Rødt Flekkefjord'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hægebostad',1034,10, (select id from lokallag where name = 'Rødt Lyngdal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Kristiansand',1001,10, (select id from lokallag where name = 'Rødt Kristiansand'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Kvinesdal',1037,10, (select id from lokallag where name = 'Rødt Flekkefjord'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Lindesnes',1029,10, (select id from lokallag where name = 'Rødt Lindesnes'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Lyngdal',1032,10, (select id from lokallag where name = 'Rødt Lyngdal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Mandal',1002,10, (select id from lokallag where name = 'Rødt Lindesnes'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Marnardal',1021,10, (select id from lokallag where name = 'Rødt Lindesnes'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sirdal',1046,10, (select id from lokallag where name = 'Rødt Flekkefjord'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Søgne',1018,10, (select id from lokallag where name = 'Rødt Kristiansand'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Songdalen',1017,10, (select id from lokallag where name = 'Rødt Kristiansand'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vennesla',1014,10, (select id from lokallag where name = 'Rødt Vennesla'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Bjerkreim',1114,11, (select id from lokallag where name = 'Rødt Dalane'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Bokn',1145,11, (select id from lokallag where name = 'Rødt Haugaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Eigersund',1101,11, (select id from lokallag where name = 'Rødt Dalane'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Finnøy',1141,11, (select id from lokallag where name = 'Rødt Stavanger'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Forsand',1129,11, (select id from lokallag where name = 'Rødt Strand'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Gjesdal',1122,11, (select id from lokallag where name = 'Rødt Sandnes'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hå',1119,11, (select id from lokallag where name = 'Raudt Hå'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Haugesund',1106,11, (select id from lokallag where name = 'Rødt Haugaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hjelmeland',1133,11, (select id from lokallag where name = 'Rødt Strand'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Karmøy',1149,11, (select id from lokallag where name = 'Rødt Haugaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Klepp',1120,11, (select id from lokallag where name = 'Raudt Time og Klepp'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Kvitsøy',1144,11, (select id from lokallag where name = 'Uten lag Rogaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Lund',1112,11, (select id from lokallag where name = 'Rødt Dalane'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Randaberg',1127,11, (select id from lokallag where name = 'Rødt Stavanger'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Rennesøy',1142,11, (select id from lokallag where name = 'Rødt Stavanger'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sandnes',1102,11, (select id from lokallag where name = 'Rødt Sandnes'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sauda',1135,11, (select id from lokallag where name = 'Uten lag Rogaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sokndal',1111,11, (select id from lokallag where name = 'Rødt Dalane'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sola',1124,11, (select id from lokallag where name = 'Rødt Sola'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Stavanger',1103,11, (select id from lokallag where name = 'Rødt Stavanger'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Strand',1130,11, (select id from lokallag where name = 'Rødt Strand'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Suldal',1134,11, (select id from lokallag where name = 'Uten lag Rogaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Time',1121,11, (select id from lokallag where name = 'Raudt Time og Klepp'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Tysvær',1146,11, (select id from lokallag where name = 'Rødt Haugaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Utsira',1151,11, (select id from lokallag where name = 'Rødt Haugaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vindafjord',1160,11, (select id from lokallag where name = 'Rødt Haugaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Askøy',1247,12, (select id from lokallag where name = 'Rødt Askøy'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Austevoll',1244,12, (select id from lokallag where name = 'Uten lag Hordaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Austrheim',1264,12, (select id from lokallag where name = 'Raudt Austrheim'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Bergen',1201,12, (select id from lokallag where name = 'Rødt Bergen'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Bømlo',1219,12, (select id from lokallag where name = 'Raudt Bømlo'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Eidfjord',1232,12, (select id from lokallag where name = 'Uten lag Hordaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Etne',1211,12, (select id from lokallag where name = 'Uten lag Hordaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Fedje',1265,12, (select id from lokallag where name = 'Uten lag Hordaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Fitjar',1222,12, (select id from lokallag where name = 'Uten lag Hordaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Fjell',1246,12, (select id from lokallag where name = 'Rødt Øygarden'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Fusa',1241,12, (select id from lokallag where name = 'Raudt Bjørnafjorden'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Granvin',1234,12, (select id from lokallag where name = 'Raudt Voss'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Jondal',1227,12, (select id from lokallag where name = 'Rødt Ullensvang'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Kvam',1238,12, (select id from lokallag where name = 'Raudt Kvam'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Kvinnherad',1224,12, (select id from lokallag where name = 'Raudt Kvinnherad'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Lindås',1263,12, (select id from lokallag where name = 'Raudt Alver'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Masfjorden',1266,12, (select id from lokallag where name = 'Uten lag Hordaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Meland',1256,12, (select id from lokallag where name = 'Raudt Alver'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Modalen',1252,12, (select id from lokallag where name = 'Uten lag Hordaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Odda',1228,12, (select id from lokallag where name = 'Rødt Ullensvang'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Os i Hordaland',1243,12, (select id from lokallag where name = 'Raudt Bjørnafjorden'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Osterøy',1253,12, (select id from lokallag where name = 'Uten lag Hordaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Øygarden',1259,12, (select id from lokallag where name = 'Rødt Øygarden'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Radøy',1260,12, (select id from lokallag where name = 'Raudt Alver'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Samnanger',1242,12, (select id from lokallag where name = 'Uten lag Hordaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Stord',1221,12, (select id from lokallag where name = 'Raudt Stord'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sund',1245,12, (select id from lokallag where name = 'Rødt Øygarden'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sveio',1216,12, (select id from lokallag where name = 'Rødt Ullensvang'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Tysnes',1223,12, (select id from lokallag where name = 'Uten lag Hordaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Ullensvang',1231,12, (select id from lokallag where name = 'Rødt Ullensvang'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Ulvik',1233,12, (select id from lokallag where name = 'Rødt Ullensvang'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vaksdal',1251,12, (select id from lokallag where name = 'Uten lag Hordaland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Voss',1235,12, (select id from lokallag where name = 'Raudt Voss'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Årdal',1424,14, (select id from lokallag where name = 'Raudt Årdal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Askvoll',1428,14, (select id from lokallag where name = 'Raudt Dalsfjorden'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Aurland',1421,14, (select id from lokallag where name = 'Raudt Lærdal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Balestrand',1418,14, (select id from lokallag where name = 'Raudt Høyanger'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Bremanger',1438,14, (select id from lokallag where name = 'Raudt Bremanger'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Eid',1443,14, (select id from lokallag where name = 'Raudt Vågsøy'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Fjaler',1429,14, (select id from lokallag where name = 'Raudt Dalsfjorden'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Flora',1401,14, (select id from lokallag where name = 'Raudt Flora'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Førde',1432,14, (select id from lokallag where name = 'Raudt Indre Sunnfjord'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Gaular',1430,14, (select id from lokallag where name = 'Raudt Indre Sunnfjord'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Gloppen',1445,14, (select id from lokallag where name = 'Raudt Gloppen'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Gulen',1411,14, (select id from lokallag where name = 'Raudt Høyanger'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hornindal',1444,14, (select id from lokallag where name = 'Uten lag Sogn og Fjordane'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Høyanger',1416,14, (select id from lokallag where name = 'Raudt Høyanger'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hyllestad',1413,14, (select id from lokallag where name = 'Uten lag Sogn og Fjordane'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Jølster',1431,14, (select id from lokallag where name = 'Raudt Indre Sunnfjord'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Lærdal',1422,14, (select id from lokallag where name = 'Raudt Lærdal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Leikanger',1419,14, (select id from lokallag where name = 'Raudt Luster'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Luster',1426,14, (select id from lokallag where name = 'Raudt Luster'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Naustdal',1433,14, (select id from lokallag where name = 'Raudt Indre Sunnfjord'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Selje',1441,14, (select id from lokallag where name = 'Raudt Vågsøy'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sogndal',1420,14, (select id from lokallag where name = 'Raudt Stryn'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Solund',1412,14, (select id from lokallag where name = 'Uten lag Sogn og Fjordane'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Stryn',1449,14, (select id from lokallag where name = 'Raudt Stryn'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vågsøy',1439,14, (select id from lokallag where name = 'Raudt Vågsøy'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vik',1417,14, (select id from lokallag where name = 'Uten lag Sogn og Fjordane'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Ålesund',1504,15, (select id from lokallag where name = 'Rødt Ålesund'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Aukra',1547,15, (select id from lokallag where name = 'Rødt Molde'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Aure',1576,15, (select id from lokallag where name = 'Rødt Kristiansund'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Averøy',1554,15, (select id from lokallag where name = 'Rødt Kristiansund'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Eide',1551,15, (select id from lokallag where name = 'Rødt Molde'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Fræna',1548,15, (select id from lokallag where name = 'Rødt Molde'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Giske',1532,15, (select id from lokallag where name = 'Rødt Ålesund'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Gjemnes',1557,15, (select id from lokallag where name = 'Rødt Molde'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Halsa',1571,15, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Haram',1534,15, (select id from lokallag where name = 'Rødt Ålesund'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hareid',1517,15, (select id from lokallag where name = 'Raudt Søre Sunnmøre'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Herøy i  Møre og Romsdal',1515,15, (select id from lokallag where name = 'Raudt Søre Sunnmøre'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Kristiansund',1505,15, (select id from lokallag where name = 'Rødt Kristiansund'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Midsund',1545,15, (select id from lokallag where name = 'Rødt Molde'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Molde',1502,15, (select id from lokallag where name = 'Rødt Molde'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Nesset',1543,15, (select id from lokallag where name = 'Rødt Molde'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Norddal',1524,15, (select id from lokallag where name = 'Rødt Ålesund'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Ørskog',1523,15, (select id from lokallag where name = 'Rødt Ålesund'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Ørsta',1520,15, (select id from lokallag where name = 'Raudt Ørsta'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Rauma',1539,15, (select id from lokallag where name = 'Rødt Molde'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sande i Møre og Romsdal',1514,15, (select id from lokallag where name = 'Raudt Søre Sunnmøre'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sandøy',1546,15, (select id from lokallag where name = 'Rødt Ålesund'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Skodje',1529,15, (select id from lokallag where name = 'Rødt Ålesund'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Smøla',1573,15, (select id from lokallag where name = 'Uten lag Møre og Romsdal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Stordal',1526,15, (select id from lokallag where name = 'Uten lag Møre og Romsdal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Stranda',1525,15, (select id from lokallag where name = 'Raudt Søre Sunnmøre'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sula',1531,15, (select id from lokallag where name = 'Rødt Ålesund'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sunndal',1563,15, (select id from lokallag where name = 'Rødt Sunndal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Surnadal',1566,15, (select id from lokallag where name = 'Rødt Sunndal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sykkylven',1528,15, (select id from lokallag where name = 'Rødt Ålesund'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Tingvoll',1560,15, (select id from lokallag where name = 'Rødt Sunndal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Ulstein',1516,15, (select id from lokallag where name = 'Raudt Ulstein'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vanylven',1511,15, (select id from lokallag where name = 'Raudt Søre Sunnmøre'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vestnes',1535,15, (select id from lokallag where name = 'Rødt Molde'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Volda',1519,15, (select id from lokallag where name = 'Raudt Søre Sunnmøre'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Aarborte – Hattfjelldal',1826,18, (select id from lokallag where name = 'Uten lag Nordland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Alstahaug',1820,18, (select id from lokallag where name = 'Rødt Alstahaug'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Andøy',1871,18, (select id from lokallag where name = 'Rødt Andøy'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Ballangen',1854,18, (select id from lokallag where name = 'Rødt Narvik'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Beiarn',1839,18, (select id from lokallag where name = 'Uten lag Nordland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Bindal',1811,18, (select id from lokallag where name = 'Rødt Vega'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Bø i Nordland',1867,18, (select id from lokallag where name = 'Rødt Sortland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Bodø',1804,18, (select id from lokallag where name = 'Rødt Bodø'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Brønnøy',1813,18, (select id from lokallag where name = 'Rødt Brønnøy'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Divtasvuodna – Tysfjord',1850,18, (select id from lokallag where name = 'Rødt Narvik'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Dønna',1827,18, (select id from lokallag where name = 'Rødt Dønna'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Evenes',1853,18, (select id from lokallag where name = 'Uten lag Nordland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Fauske – Fuossko',1841,18, (select id from lokallag where name = 'Rødt Fauske'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Flakstad',1859,18, (select id from lokallag where name = 'Rødt Vestvågøy'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Gildeskål',1838,18, (select id from lokallag where name = 'Rødt Bodø'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Grane',1825,18, (select id from lokallag where name = 'Uten lag Nordland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hadsel',1866,18, (select id from lokallag where name = 'Rødt Hadsel'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hamarøy – Hábmer',1849,18, (select id from lokallag where name = 'Rødt Hamarøy'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hemnes',1832,18, (select id from lokallag where name = 'Rødt Hemnes'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Herøy i Nordland',1818,18, (select id from lokallag where name = 'Rødt Alstahaug'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Leirfjord',1822,18, (select id from lokallag where name = 'Rødt Leirfjord'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Lødingen',1851,18, (select id from lokallag where name = 'Rødt Sortland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Lurøy',1834,18, (select id from lokallag where name = 'Uten lag Nordland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Meløy',1837,18, (select id from lokallag where name = 'Rødt Bodø'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Moskenes',1874,18, (select id from lokallag where name = 'Rødt Vestvågøy'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Narvik',1805,18, (select id from lokallag where name = 'Rødt Narvik'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Nesna',1828,18, (select id from lokallag where name = 'Rødt Nesna'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Øksnes',1868,18, (select id from lokallag where name = 'Rødt Øksnes'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Rana',1833,18, (select id from lokallag where name = 'Rødt Rana'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Rødøy',1836,18, (select id from lokallag where name = 'Rødt Rana'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Røst',1856,18, (select id from lokallag where name = 'Rødt Røst'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Saltdal',1840,18, (select id from lokallag where name = 'Rødt Saltdal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sømna',1812,18, (select id from lokallag where name = 'Rødt Sømna'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sørfold',1845,18, (select id from lokallag where name = 'Rødt Sørfold'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sortland – Suortá',1870,18, (select id from lokallag where name = 'Rødt Sortland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Steigen',1848,18, (select id from lokallag where name = 'Rødt Steigen'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Tjeldsund',1852,18, (select id from lokallag where name = 'Uten lag Troms'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Træna',1835,18, (select id from lokallag where name = 'Uten lag Nordland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Værøy',1857,18, (select id from lokallag where name = 'Uten lag Nordland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vågan',1865,18, (select id from lokallag where name = 'Rødt Vågan'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vefsn',1824,18, (select id from lokallag where name = 'Rødt Vefsn'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vega',1815,18, (select id from lokallag where name = 'Rødt Vega'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vestvågøy',1860,18, (select id from lokallag where name = 'Rødt Vestvågøy'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vevelstad',1816,18, (select id from lokallag where name = 'Rødt Vega'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Balsfjord',1933,19, (select id from lokallag where name = 'Rødt Balsfjord'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Bardu',1922,19, (select id from lokallag where name = 'Uten lag Troms'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Berg',1929,19, (select id from lokallag where name = 'Uten lag Troms'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Dyrøy',1926,19, (select id from lokallag where name = 'Uten lag Troms'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Gáivuotna – Kåfjord – Kaivuono',1940,19, (select id from lokallag where name = 'Uten lag TRoms'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Gratangen',1919,19, (select id from lokallag where name = 'Uten lag Troms'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Harstad – Hárstták',1903,19, (select id from lokallag where name = 'Rødt Harstad'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Ibestad',1917,19, (select id from lokallag where name = 'Uten lag Troms'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Karlsøy',1936,19, (select id from lokallag where name = 'Rødt Tromsø'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Kvæfjord',1911,19, (select id from lokallag where name = 'Rødt Harstad'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Kvænangen',1943,19, (select id from lokallag where name = 'Uten lag Troms'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Lenvik',1931,19, (select id from lokallag where name = 'Uten lag Troms'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Loabák – Lavangen',1920,19, (select id from lokallag where name = 'Uten lag Troms'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Lyngen',1938,19, (select id from lokallag where name = 'Uten lag Troms'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Målselv',1924,19, (select id from lokallag where name = 'Rødt Målselv'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Nordreisa – Ráisa – Raisi',1942,19, (select id from lokallag where name = 'Uten lag Troms'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Salangen',1923,19, (select id from lokallag where name = 'Rødt Salangen'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Skånland',1913,19, (select id from lokallag where name = 'Rødt Harstad'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Skjervøy',1941,19, (select id from lokallag where name = 'Rødt Skjervøy'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sørreisa',1925,19, (select id from lokallag where name = 'Uten lag Troms'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Storfjord – Omasvuotna – Omasvuono',1939,19, (select id from lokallag where name = 'Rødt Balsfjord'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Torsken',1928,19, (select id from lokallag where name = 'Uten lag Troms'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Tranøy',1927,19, (select id from lokallag where name = 'Uten lag Troms'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Tromsø',1902,19, (select id from lokallag where name = 'Rødt Tromsø'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Alta',2012,20, (select id from lokallag where name = 'Rødt Alta'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Båtsfjord',2028,20, (select id from lokallag where name = 'Uten lag Finnmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Berlevåg',2024,20, (select id from lokallag where name = 'Uten lag Finnmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Deatnu – Tana',2025,20, (select id from lokallag where name = 'Uten lag Finnmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Gamvik',2023,20, (select id from lokallag where name = 'Uten lag Finnmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Guovdageaidnu – Kautokeino',2011,20, (select id from lokallag where name = 'Uten lag Finnmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hammerfest',2004,20, (select id from lokallag where name = 'Rødt Hammerfest'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hasvik',2015,20, (select id from lokallag where name = 'Uten lag Finnmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Kárášjohka – Karasjok',2021,20, (select id from lokallag where name = 'Uten lag Finnmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Kvalsund',2017,20, (select id from lokallag where name = 'Uten lag Finnmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Lebesby',2022,20, (select id from lokallag where name = 'Uten lag Finnmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Loppa',2014,20, (select id from lokallag where name = 'Uten lag Finnmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Måsøy',2018,20, (select id from lokallag where name = 'Rødt Hammerfest'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Nordkapp',2019,20, (select id from lokallag where name = 'Uten lag Finnmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Porsanger – Porsángu – Porsanki',2020,20, (select id from lokallag where name = 'Uten lag Finnmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Sør-Varanger',2030,20, (select id from lokallag where name = 'Rødt Sør-Varanger'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Unjárga – Nesseby',2027,20, (select id from lokallag where name = 'Uten lag Finnmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vadsø',2003,20, (select id from lokallag where name = 'Rødt Vadsø'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vardø',2002,20, (select id from lokallag where name = 'Uten lag Finnmark'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Svalbard',2100,21, (select id from lokallag where name = 'Uten lag Troms'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Jan Mayen',2211,21, (select id from lokallag where name = 'Uten lag Troms'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Åfjord',5018,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Agdenes',5016,50, (select id from lokallag where name = 'Rødt Orkland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Bjugn',5017,50, (select id from lokallag where name = 'Rødt Ørland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Flatanger',5049,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Fosnes',5048,50, (select id from lokallag where name = 'Rødt Namsos'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Frosta',5036,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Frøya',5014,50, (select id from lokallag where name = 'Rødt Frøya'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Grong',5045,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hemne',5011,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Hitra',5013,50, (select id from lokallag where name = 'Rødt Hitra'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Holtålen',5026,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Høylandet',5046,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Inderøy',5053,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Indre Fosen',5054,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Klæbu',5030,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Leka',5052,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Levanger',5037,50, (select id from lokallag where name = 'Rødt Levanger'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Lierne',5042,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Malvik',5031,50, (select id from lokallag where name = 'Rødt Malvik'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Meldal',5023,50, (select id from lokallag where name = 'Rødt Orkland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Melhus',5028,50, (select id from lokallag where name = 'Rødt Melhus'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Meråker',5034,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Midtre Gauldal',5027,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Nærøy',5051,50, (select id from lokallag where name = 'Rødt Nærøysund'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Namdalseid',5040,50, (select id from lokallag where name = 'Rødt Namsos'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Namsos',5005,50, (select id from lokallag where name = 'Rødt Namsos'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Namsskogan',5044,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Oppdal',5021,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Orkdal',5024,50, (select id from lokallag where name = 'Rødt Orkland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Ørland',5015,50, (select id from lokallag where name = 'Rødt Ørland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Osen',5020,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Overhalla',5047,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Raarvikhe – Røyrvik',5043,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Rennebu',5022,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Rindal',5061,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Roan',5019,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Røros',5025,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Selbu',5032,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Skaun',5029,50, (select id from lokallag where name = 'Rødt Skaun'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Snåase – Snåsa',5041,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Snillfjord',5012,50, (select id from lokallag where name = 'Rødt Orkland'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Steinkjer',5004,50, (select id from lokallag where name = 'Rødt Steinkjer'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Stjørdal',5035,50, (select id from lokallag where name = 'Rødt Stjørdal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Trondheim',5001,50, (select id from lokallag where name = 'Rødt Trondheim'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Tydal',5033,50, (select id from lokallag where name = 'Uten lag Trøndelag'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Verdal',5038,50, (select id from lokallag where name = 'Rødt Verdal'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Verran',5039,50, (select id from lokallag where name = 'Rødt Steinkjer'));
    INSERT INTO kommune(navn,nummer,fylke_id,lokallag_id) VALUES ('Vikna',5050,50, (select id from lokallag where name = 'Rødt Nærøysund'));

-- --------------------------------------------------------

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