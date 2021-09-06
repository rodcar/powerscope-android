package pe.chiararodriguez.amelia.network.responses

import java.io.Serializable

data class SignInResponse(var email: String?, var id: Int, var accessToken: String?) :
    Serializable {
    constructor() : this("", 0, "")
}