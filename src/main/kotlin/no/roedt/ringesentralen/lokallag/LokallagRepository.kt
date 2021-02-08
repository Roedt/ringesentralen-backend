package no.roedt.ringesentralen.lokallag

import io.quarkus.hibernate.orm.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class LokallagRepository : PanacheRepository<Lokallag> {
}