package no.roedt.hypersys.login

import jakarta.enterprise.context.Dependent

@Dependent
class LoginService(internal val repository: LoginAttemptRepository) {
    fun slett(hypersysID: Int?) = repository.delete("hypersysID=?1", hypersysID)
    fun persist(loginAttempt: LoginAttempt) = repository.persist(loginAttempt)
}
