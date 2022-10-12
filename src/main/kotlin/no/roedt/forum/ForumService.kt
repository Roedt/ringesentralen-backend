package no.roedt.forum

import no.roedt.forum.underforum.Traad
import no.roedt.forum.underforum.Underforum
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ForumService {
    fun hentAlleUnderforum(): List<Underforum> {
        return listOf(Underforum(id = 1, tittel = "Test"))
    }

    fun hentTraader(underforum: Underforum): List<Traad> = listOf(Traad(tittel = "Første tråd"))
}
