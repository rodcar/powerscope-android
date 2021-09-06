package pe.powerscope.app.util

import android.view.View
import android.widget.ProgressBar

class LoadingUtil {
    companion object {
        var isDisplayed = false
        lateinit var progressBar: ProgressBar

        fun show() {
            progressBar.visibility = View.VISIBLE
            isDisplayed = true
        }

        fun hide() {
            progressBar.visibility = View.GONE
            isDisplayed = false
        }
    }
}