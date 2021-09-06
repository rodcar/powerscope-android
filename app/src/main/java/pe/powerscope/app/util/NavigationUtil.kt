package pe.powerscope.app.util

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import pe.powerscope.app.R
import java.io.Serializable

class NavigationUtil {
    companion object {
        fun openClear(context: Context, cls: Class<*>) {
            val intent = Intent(context, cls)
            /*intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP*/
            context.startActivity(intent)
        }

        fun open(context: Context, cls: Class<*>) {
            val intent = Intent(context, cls)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            context.startActivity(intent)
        }

        /*fun openWithItem(context: Context, cls: Class<*>, item: Serializable, accessToken: String) {
            val intent = Intent(context, cls)
            intent.putExtra(context.getString(R.string.intent_item), item)
            intent.putExtra(context.getString(R.string.saved_user_token_key), accessToken)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            context.startActivity(intent)
        }*/

        fun openWithToken(context: Context, cls: Class<*>, accessToken: String) {
            val intent = Intent(context, cls)
            intent.putExtra(context.getString(R.string.saved_user_token_key), accessToken)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            context.startActivity(intent)
        }

        fun openWithTokenAndResult(
            appCompatActivity: AppCompatActivity,
            context: Context,
            cls: Class<*>,
            accessToken: String
        ) {
            val intent = Intent(appCompatActivity, cls)
            intent.putExtra(context.getString(R.string.saved_user_token_key), accessToken)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            appCompatActivity.startActivityForResult(intent, 1)
        }

        fun openWithTokenAndResult(
            appCompatActivity: AppCompatActivity,
            context: Context,
            cls: Class<*>,
            accessToken: String,
            requestCode: Int
        ) {
            val intent = Intent(appCompatActivity, cls)
            intent.putExtra(context.getString(R.string.saved_user_token_key), accessToken)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            appCompatActivity.startActivityForResult(intent, requestCode)
        }

        fun openWithTokenAndResult(
            fragmentActivity: FragmentActivity,
            context: Context,
            cls: Class<*>,
            accessToken: String,
            requestCode: Int
        ) {
            val intent = Intent(fragmentActivity, cls)
            intent.putExtra(context.getString(R.string.saved_user_token_key), accessToken)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            fragmentActivity.startActivityForResult(intent, requestCode)
        }

        /*fun openWithItemAndResult(
            fragmentActivity: FragmentActivity,
            context: Context,
            cls: Class<*>,
            item: Serializable,
            accessToken: String,
            requestCode: Int
        ) {
            val intent = Intent(fragmentActivity, cls)
            intent.putExtra(context.getString(R.string.intent_item), item)
            intent.putExtra(context.getString(R.string.saved_user_token_key), accessToken)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            fragmentActivity.startActivityForResult(intent, requestCode)
        }*/
    }
}