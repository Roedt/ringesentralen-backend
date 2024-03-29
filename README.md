
# Ringesentralen

Backend for Ringesentralen v2.

## Føresetnadar
### Alternativ A (enklast å setje opp)
- Tilgang til Ringesentralen-prosjektet på Google Cloud Platform

### Alternativ B
- Sett opp eit eiget prosjekt på Google Cloud Platform
  - Sett opp Secret Manager med variablane som er lista opp i GCPSecretManagerKey-enumen
  - Merk at både frontendTokenKey, frontendSystembruker, frontendSystembrukerPassord og encryptionKey korresponderer med verdiar i frontenden.
- Endre i application.properties så _secretManagerProjectId_ peikar på ditt nye prosjekt
- Generér matchande private- og publickey, og sørg for at dei ligg i resources\META-INF.resources 

## Utvikling
1. `docker-compose -f mysql.yaml down -v && docker-compose -f mysql.yaml up` (startar databasen frå tom tilstand, køyrer alle databaseskript og legg inn eksempelpersonar)
1. Autentiser deg mot [Google Cloud Platform](https://cloud.google.com/docs/authentication/getting-started)[1], ringesentralen-prosjektet[2].
   - Dette inneber typisk at du må køyre `export GOOGLE_APPLICATION_CREDENTIALS="/sti/til/jsonfil/frå/gcp"
1. Sørg for å ha _hypersysBaseUrl_ i .env-fila. (Eventuelt som mijøvariabel, f.eks. i ~/.bash_profile). I .env bør du overstyre _hypersysBaseUrl_, _usePrivateKeyFromSecretManager_ til false og _quarkus.flyway.locations=db/migration,filesystem:src/test/resources/db/migration_ for å setje opp testdata.
1. mvn compile quarkus:dev
1. No køyrer applikasjonen på port 8080. 
   - Swagger UI er tilgjengeleg på http://localhost:8080/swagger-ui, 
   - OpenAPI-definisjonar på http://localhost:8080/openapi
1. sql\toemRingeinfo.sql tilbakestiller registrerte samtalar, så du startar på scratch igjen

Punkt 1 startar databasen på port 3306, og admin-panelet for databasen på port 9080.

[1]: Systemet baserer seg på innlogging via Hypersys. URL hit, pluss klient-idar og -løyndommar, må definerast på eit eller anna vis som miljøvariablar. Desse er allereie satt opp i Secret Manager på GCP, så det enklaste er å bruke denne. Alternativt kan du setje opp alt sjølv, viss du vil det.

[2] Ein admin må først gi deg tilgang til prosjektet. Ønskjer du å teste ting utan å ha tilgang til Hypersys, kan du kommentere ut Hypersys-koden i /token/login, men det vil gjera at ein del ting ikkje kjem til å funke.

## Deploy til prod
Push til main-greina startar automatisk eit bygg på Google Cloud Build, som gjer mvn package, lager Docker-image og deployar det til Google Cloud Run.

## Implementasjonen
Systemet er bygd i Kotlin, basert på appserveren Quarkus

Det er tilpassa til å køyre på Google Cloud Platform, primært da Google Cloud Run. I tillegg ligg databasen som ein mysql-database i _CloudSQL_, og alt av secrets ligg i _Secret Manager_.

MicroProfile JWT er brukt for å sikre endepunkta. Det som er gjort her er dels basert på ymse guidar, men kombinasjonen Quarkus, Kotlin, MP-JWT og Swagger-UI viste seg å ikkje vera veldig utbreidd, så det ligg også mykje eksperimentering bak.

## Kopling frå klient til server
Flyten er omtrent sånn:

1. Frontend kallar _/token/login_ på backend, med parametre brukarnamn og passord frå brukaren og ein eigen (hardkoda) "klient-key". Klient-key er definert som miljøvariabel serverside. Bør kanskje vurdere å gjera om denne til secret manager-secret
1. Backenden gjer kall vidare mot hypersys-login, og får eit token tilbake
1. Backenden lager sitt eiget token, og legg inn alt frå hypersys-tokenet som claims der
1. Klienten får dette tokenet, og bruker det (som bearer token i authorization i header) på alle resterande kall mot backenden
1. Backenden transformerer det til hypersys-token on demand ved vidare kall mot hypersys

## GCP-oppsettet
- Frontenden er sett opp på Cloud Run
- Backenden er sett opp på Cloud Run
- Databasen er i Cloud SQL, med MySQL 8.0
- Kommunikasjonen mellom frontend og backend går via Serverless VPC Connector, kor vi stort sett har følgd guiden i GCP-dokumentasjonen
- Løyndommar er sett opp i GCP Secret Manager
- Både frontend og backend byggjast i Cloud Build. Per no er det cloudbuild-test.yaml som brukast for begge to. Denne gjer bygging frå scratch med maven/npm. For backenden har byggejobben fått veldig mykje ressursar på Cloud Build. Backenden køyrer i Quarkus native-modus, som gjer at oppstartstida er minimal og applikasjonen svarer raskt. Ulempa er at byggjing av native-applikasjonar er veldig kraft-krevjande, og tar litt tid, vanlegvis rundt 5-6 minutt. For brukarane er imidlertid forskjellen i responstid stor.