package no.roedt.ringesentralen.brukere

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FylkeRepository : PanacheRepositoryBase<Fylke, Int>