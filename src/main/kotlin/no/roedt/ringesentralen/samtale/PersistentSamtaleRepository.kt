package no.roedt.ringesentralen.samtale

import io.quarkus.hibernate.orm.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PersistentSamtaleRepository : PanacheRepository<PersistentSamtale>