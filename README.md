# Ringesentralen

Backend for Ringesentralen.

## Utvikling
1. `docker-compose -f mysql.yaml down -v && docker-compose -f mysql.yaml up` (startar databasen frå tom tilstand, køyrer alle databaseskript og legg inn eksempelpersonar)
1. mvn compile quarkus:dev
1. No køyrer applikasjonen på port 8080. Swagger UI er tilgjengeleg på http://localhost:8080/swagger-ui, OpenAPI-definisjonar på http://localhost:8080/openapi
1. sql\toemRingeinfo.sql tilbakestiller registrerte samtalar, så du startar på scratch igjen

Punkt 1 startar databasen på port 3306, og admin-panelet for databasen på port 9080.

NB: Systemet baserer seg på innlogging via Hypersys. URL hit, pluss klient-idar og -løyndommar, må definerast på eit eller anna vis som miljøvariablar. 
Da med namn som definert i HypersysEndpoint.

## Deploy til prod
Push til main-greina startar automatisk eit bygg på Google Cloud Build, som gjer mvn package, lager Docker-image og deployar det til Google Cloud Run

## Teknisk
Systemet er bygd i Kotlin, basert på appserveren Quarkus

## Kopling frå klient til server
Flyten er omtrent sånn:

1. Klienten kallar /token, med _key_ som er avtalt mellom klient og server på forhand (og definert som miljøvariabel serverside)
   som er ei post-teneste for å kunne halde. Da får klienten ein token tilbake, basert på serverens både private- og public-key
1. Alle andre kall til serveren krev at dette tokenet sendast med
1. TODO: Finn ut korleis dette korresponderer med kall mot hypersys og tokens derifrå