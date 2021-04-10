package no.roedt.ringesentralen

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.runtime.annotations.RegisterForReflection
import javax.enterprise.context.Dependent
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "postnummerIKommunerMedFleireLag")
@Cacheable
@RegisterForReflection
data class PostnummerIKommunerMedFleireLag(
    var postnummerFra: Int,
    var postnummerTil: Int,
    var lokallag: Int,
    var fylke: Int
) : PanacheEntity() {
    constructor() : this(
        postnummerFra = 0,
        postnummerTil = 0,
        lokallag = 0,
        fylke = 0
    )
}

@Dependent
class PostnummerIKommunerMedFleireLagRepository : PanacheRepository<PostnummerIKommunerMedFleireLag>