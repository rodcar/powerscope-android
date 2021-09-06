package pe.powerscope.app.network.requests

import java.io.Serializable

data class SignUpRequest(
    var email: String?,
    var password: String?,
    var name: String?,
    var lastName: String?
) : Serializable {
    constructor() : this("", "", "", "")
}