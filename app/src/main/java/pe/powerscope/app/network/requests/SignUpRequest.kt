package pe.chiararodriguez.amelia.network.requests

import java.io.Serializable

data class SignUpRequest(
    var email: String?,
    var password: String?,
    var dateOfBirth: String?,
    var gender: String?,
    var username: String?,
    var profileImageURL: String?
) : Serializable {
    constructor() : this("", "", "", "", "", "")
}