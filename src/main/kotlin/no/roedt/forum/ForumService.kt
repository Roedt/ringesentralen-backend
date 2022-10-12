package no.roedt.forum

import no.roedt.forum.database.ForumRepository
import no.roedt.forum.underforum.Traad
import no.roedt.forum.underforum.Underforum
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ForumService(
    val forumRepository: ForumRepository
) {
    fun hentAlleUnderforum(): List<Underforum> = forumRepository.hentAlleUnderforum()

    fun hentTraader(underforum: Underforum): List<Traad> = forumRepository.hentTraader(underforum)
}
