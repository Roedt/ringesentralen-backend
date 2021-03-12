package no.roedt.ringesentralen.hypersys

import io.quarkus.hibernate.orm.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class LoginAttemptRepository : PanacheRepository<LoginAttempt>