INSERT INTO person (`fornavn`, `etternavn`, `telefonnummer`, `postnummer`, `email`, `fylke`, `groupID`, `lokallag`,
                    `hypersysID`, `kilde`)
VALUES ('Tidligere', 'Medlem', '-2', -1, null, -1, 4, -1, -3, 'Systembruker');

INSERT INTO ringer (personId)
VALUES ((select id from `person` where fornavn = 'Tidligere' and etternavn = 'Medlem'));
