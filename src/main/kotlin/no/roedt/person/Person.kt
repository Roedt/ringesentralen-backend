package no.roedt.person

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.Kilde
import no.roedt.Kommune
import no.roedt.RoedtPanacheEntity
import no.roedt.brukere.GroupID
import no.roedt.ringesentralen.brukere.RingesentralenGroupID
import java.time.Instant
import javax.enterprise.inject.spi.CDI
import javax.persistence.Basic
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "person")
@Cacheable
@RegisterForReflection
data class Person(
    var hypersysID: Int?,
    var fornavn: String,
    var etternavn: String,
    var telefonnummer: String?,
    var email: String?,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "postnummer", nullable = false)
    @JsonSerialize(using = PostnummerSerializer::class)
    @JsonDeserialize(using = PostnummerDeserializer::class)
    var postnummer: Postnummer,
    var fylke: Int,
    var lokallag: Int,
    private var groupID: Int, // TODO: Enumerated type number/ordinal
    @Enumerated(EnumType.STRING) var kilde: Kilde,
    @Basic
    var sistOppdatert: Instant?
) : RoedtPanacheEntity() {
    fun isSystembruker(): Boolean = fornavn == "Systembruker" && etternavn == "Frontend"

    constructor() : this(
        hypersysID = null,
        fornavn = "",
        etternavn = "",
        telefonnummer = "",
        email = null,
        postnummer = Postnummer("", "", Kommune("", "", 0, 0)),
        fylke = -1,
        groupID = 0,
        lokallag = 1,
        kilde = Kilde.Verva,
        sistOppdatert = null
    )

    fun groupID() = groupID

    private fun setGroupID(groupID: Int) {
        if (RingesentralenGroupID.isBrukerEllerVenter(this.groupID) && RingesentralenGroupID.isIkkeRegistrertRinger(
                groupID
            )
        ) {
            throw RuntimeException("$id er allerede ringer, og kan ikke settes til ei ikke-ringer-rolle.")
        }
        this.groupID = groupID
    }

    fun setGroupID(groupID: GroupID) = setGroupID(groupID.nr)
}

object PostnummerSerializer : JsonSerializer<Postnummer>() {
    override fun serialize(postnummer: Postnummer, gen: JsonGenerator, serializers: SerializerProvider) {
        with(gen) {
            writeString(postnummer.Postnummer)
        }
    }
}

object PostnummerDeserializer : JsonDeserializer<Postnummer>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): Postnummer {
        val repo = CDI.current().select(PostnummerRepository::class.java).get()
        val tree: JsonNode = parser.codec.readTree(parser)
        val postnr = tree.get("postnummer").textValue()
        return repo.findById(postnr)
    }
}
