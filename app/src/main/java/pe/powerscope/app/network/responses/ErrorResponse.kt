package pe.powerscope.app.network.responses

import java.io.Serializable

class ErrorResponse(var message: String?) : Serializable {
    constructor() : this("")
}