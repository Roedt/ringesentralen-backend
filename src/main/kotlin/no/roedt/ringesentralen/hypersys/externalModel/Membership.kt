package no.roedt.ringesentralen.hypersys.externalModel

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Membership(
        @JsonProperty("organisation") val organisation: Int,
        @JsonProperty("organisation_name") val organisationName: String,
        @JsonProperty("created") val created: String, // Ser kanskje ut som timestamp-format? "2020-10-26T11:24:36.479475+01:00
        @JsonProperty("start_date") val startDate: String, // Bør vera LocalDate, men orkar ikkje starte med custom-deserialisering utan at det er naudsynt
        @JsonProperty("end_date") val endDate: String,
        @JsonProperty("extra_organisations") val extraOrganisations: List<Any>
)