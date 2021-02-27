package no.roedt.frivilligsystem.kontakt

import io.quarkus.hibernate.orm.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class KontaktRepository : PanacheRepository<Kontakt>