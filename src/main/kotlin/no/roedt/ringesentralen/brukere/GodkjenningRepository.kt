package no.roedt.ringesentralen.brukere

import io.quarkus.hibernate.orm.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class GodkjenningRepository : PanacheRepository<Godkjenning>