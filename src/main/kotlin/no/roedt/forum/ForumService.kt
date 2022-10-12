package no.roedt.forum

import no.roedt.forum.underforum.Underforum
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ForumService {
    fun hentAlleUnderforum(): List<Underforum> {
        return listOf()
    }
}
