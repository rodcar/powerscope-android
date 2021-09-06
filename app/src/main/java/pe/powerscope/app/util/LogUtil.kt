package pe.powerscope.app.util

import android.util.Log

class LogUtil {
    companion object {
        fun e(tag: String, t: Throwable) {
            t.apply {
                Log.e(tag, "Message: ${message}\nLocalized Message: ${localizedMessage}")
                printStackTrace()
            }
        }
    }
}