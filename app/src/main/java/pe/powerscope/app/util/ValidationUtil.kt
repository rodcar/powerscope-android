package ppe.powerscope.app.util

import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation

class ValidationUtil {
    companion object {
        val EmptyValidation = SimpleCustomValidation {
            it.trim().length > 0
        }
    }
}