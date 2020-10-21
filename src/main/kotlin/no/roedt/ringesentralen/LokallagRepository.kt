package no.roedt.ringesentralen

import io.quarkus.hibernate.orm.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class LokallagRepository : PanacheRepository<Lokallag> {
}