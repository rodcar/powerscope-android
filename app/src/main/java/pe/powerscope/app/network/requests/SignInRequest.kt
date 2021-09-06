package pe.chiararodriguez.amelia.network.requests

import java.io.Serializable

data class SignInRequest(
    var email: String?,
    var password: String?
) : Serializable {
    constructor() : this("", "")
}