package no.roedt.ringesentralen.hypersys.externalModel

import com.fasterxml.jackson.annotation.JsonProperty

data class Profile(
        @JsonProperty("user") val user: User
)

/*
"{"user":{"id":15421,"name":"Mads Opheim",
"email":"mads.opheim@gmail.com","addresses":[],
"phone":"","phone2":"","roles":[],
"memberships":[{"id":10837,"member":15421,
    "organisation":1,
    "organisation_name":"Organisasjon1 sentralt","paid":0,
    "paid_sum":"50.00","fee":2,"fee_type":"Vanlig","KID":"0015421019",
    "to_account":"99991111111","created":"2020-10-26T11:24:36.479475+01:00","start_date":"2020-10-26",
    "end_date":"2020-12-31","extra_organisations":[]}]}}";
 */