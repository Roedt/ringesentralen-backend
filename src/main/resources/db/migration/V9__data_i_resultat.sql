INSERT INTO resultat (id, navn, displaytext, skalSkjules, svarte)
VALUES (0, 'Ikke svar', 'Ikke svar', false, false),
       (1, 'Vil ikke ringes før valget, men gjerne etterpå', 'Vil ikke ringes før valget, men gjerne etterpå', false, true),
       (2, 'Trenger ikke å bli oppringt igjen (slettes fra alle lister)', 'Trenger ikke å bli oppringt igjen', false, true),
       (3, 'Trenger oppfølging av toppkandidat (husk å skrive stikkord om tema i kommentarfeltet)', 'Trenger oppfølging', false, true),
       (4, 'Passet ikke, må bli oppringt på spesifikt tidspunkt', 'Ba om å bli oppringt på spesifikt tidspunkt.', false, true),
       (5, 'Vil bli valgkampfrivillig og aktiv i et lokallag', 'Vil bli valgkampfrivillig og aktiv i et lokallag', false, true),
       (6, 'Vil bli valgkampfrivillig (henvis til rødt.no/frivillig, og si at de også får en e-post med info)',
        'Vil bli valgkampfrivillig (henvis til rødt.no/frivillig, og si at de også får en e-post med info om hvordan man kan melde seg.)',
        false, true),
       (7, 'Nei', 'Nei', false, true),
       (8,
        'Vil bli aktiv i et lokallag(Vi følger opp aktivt med nærmeste lokallag. Om de sier at det ikke finnes lokallag der de bor, fortell hvordan man kan melde seg som kontaktperson og starte lokallag).',
        'Vil bli aktiv i et lokallag(Vi følger opp aktivt med nærmeste lokallag. Om de sier at det ikke finnes lokallag der de bor, fortell hvordan man kan melde seg som kontaktperson og starte lokallag).',
        false, true),
       (9, 'Samtale startet', 'Samtale startet', true, false),
       (10, 'Flere enn to ikke-svar', 'Flere enn to ikke-svar', false, false),
       (11, 'Svarte', 'Svarte', false, true),
       (12, 'Ring tilbake', 'Ring tilbake', false, true),
       (13, 'Ugyldig svar', 'Ugyldig svar', false, true)
ON CONFLICT DO NOTHING;