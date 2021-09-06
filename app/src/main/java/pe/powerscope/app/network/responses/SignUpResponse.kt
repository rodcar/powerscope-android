package pe.chiararodriguez.amelia.network.responses

import java.io.Serializable

data class SignUpResponse(
    var message: String?
) : Serializable {
    constructor() : this("")
}