package no.roedt.forum

import no.roedt.forum.database.ForumRepository
import no.roedt.forum.underforum.Traad
import no.roedt.forum.underforum.TraadRequest
import no.roedt.forum.underforum.Underforum
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ForumService(
    val forumRepository: ForumRepository
) {
    fun hentAlleUnderforum(): List<Underforum> = forumRepository.hentAlleUnderforum()
    fun hentTraader(underforum: String): List<Traad> = forumRepository.hentTraader(underforum)
    fun hentTraad(traad: Traad) = forumRepository.hentTraad(traad)
    fun opprettTraad(traad: TraadRequest) = forumRepository.opprettTraad(traad)
}
