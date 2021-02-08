package no.roedt.ringesentralen

import io.quarkus.hibernate.orm.panache.PanacheRepository
import no.roedt.ringesentralen.person.Person
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PersonRepository : PanacheRepository<Person> {
}