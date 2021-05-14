package no.roedt.ringesentralen.samtale.resultat

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    Type(value = Valg21SpesifikkeResultat::class, name = "Valg21SpesifikkeResultat")
)
interface ModusspesifikkeResultat
