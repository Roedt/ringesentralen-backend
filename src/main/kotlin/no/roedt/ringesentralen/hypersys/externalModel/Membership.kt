package no.roedt.ringesentralen.hypersys.externalModel

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Membership(
        @JsonProperty("organisation") val organisation: Int,
        @JsonProperty("organisation_name") val organisationName: String,
        @JsonProperty("paid") val paid: Int,
        @JsonProperty("paid_sum") val paidSum: String, // Burde kanskje ha ein eigen type for dette, men usikker på om vi i det heile tatt treng denne, så gjer ikkje det no
        @JsonProperty("fee") val fee: Int,
        @JsonProperty("fee_type") val feeType: String,
        @JsonProperty("KID") val kid: String,
        @JsonProperty("to_account") val toAccount: String, // Samme kommentar som paid_sum
        @JsonProperty("created") val created: String, // Ser kanskje ut som timestamp-format? "2020-10-26T11:24:36.479475+01:00
        @JsonProperty("start_date") val startDate: String, // Bør vera LocalDate, men orkar ikkje starte med custom-deserialisering utan at det er naudsynt
        @JsonProperty("end_date") val endDate: String,
        @JsonProperty("extra_organisations") val extraOrganisations: List<Any>
)