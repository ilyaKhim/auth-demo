import com.fasterxml.jackson.annotation.JsonProperty

data class AuthenticationResponse(
    @JsonProperty
    private val accessToken: String? = null,
    @JsonProperty
    private val refreshToken: String? = null,
    @JsonProperty
    private val type: String = "Bearer",
)
