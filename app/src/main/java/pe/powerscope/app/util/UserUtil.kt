package pe.powerscope.app.util

import android.content.Context
import pe.powerscope.app.R

class UserUtil {
    companion object {
        fun isAuthenticated(context: Context?): Boolean {
            context?.let {
                val sharedPref = it.getSharedPreferences(
                    context.getString(R.string.preference_file_key),
                    Context.MODE_PRIVATE
                )

                val userToken =
                    sharedPref.getString(it.getString(R.string.saved_user_token_key), "")

                return !userToken.equals("")
            }
            return false
        }

        /*

        fun authenticate(context: Context, id: Int, token: String) {
            context.apply {
                val sharedPref = getSharedPreferences(
                    getString(R.string.preference_file_key),
                    Context.MODE_PRIVATE
                )

                with(sharedPref.edit()) {
                    putString(
                        getString(pe.chiararodriguez.administra_t.R.string.saved_user_token_key),
                        token
                    )
                    putInt(
                        getString(pe.chiararodriguez.administra_t.R.string.saved_user_id_key),
                        id
                    )
                    commit()
                }
            }
        }

        fun getId(context: Context): Int {
            context.apply {
                val sharedPref = getSharedPreferences(
                    getString(R.string.preference_file_key),
                    Context.MODE_PRIVATE
                )

                return sharedPref.getInt(getString(R.string.saved_user_id_key), -1)
            }
            return -1
        }

        fun getAccessToken(context: Context): String {
            context.apply {
                val sharedPref = getSharedPreferences(
                    getString(R.string.preference_file_key),
                    Context.MODE_PRIVATE
                )

                return "Bearer ${
                    sharedPref.getString(
                        getString(R.string.saved_user_token_key),
                        ""
                    )
                }"
            }
            return ""
        }

         */
    }
}