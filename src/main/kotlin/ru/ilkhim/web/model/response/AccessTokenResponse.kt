package ru.ilkhim.web.model.response

import com.fasterxml.jackson.annotation.JsonProperty

data class AccessTokenResponse(
    @JsonProperty
    private val accessToken: String? = null,
)
