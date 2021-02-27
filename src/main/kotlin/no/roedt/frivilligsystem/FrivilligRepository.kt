package no.roedt.frivilligsystem

import io.quarkus.hibernate.orm.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FrivilligRepository : PanacheRepository<Frivillig>