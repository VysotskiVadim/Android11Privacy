package by.android.academy.privacy

import android.annotation.TargetApi
import android.app.AppOpsManager
import android.app.AsyncNotedAppOp
import android.app.SyncNotedAppOp
import android.content.Context
import android.os.Build
import android.util.Log

fun Context.setupDataAuditionCompat() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        setupDataAudition()
    }
}

@TargetApi(Build.VERSION_CODES.R)
fun Context.setupDataAudition() {
    val appOpsCallback = object : AppOpsManager.OnOpNotedCallback() {

        override fun onNoted(syncNotedAppOp: SyncNotedAppOp) {
            if (attributionTag != null) {
                log(
                    syncNotedAppOp.op,
                    syncNotedAppOp.attributionTag,
                    Throwable().stackTrace.joinToString(separator = "\n")
                )
            } else {
                logE(
                    syncNotedAppOp.op,
                    Throwable().stackTrace.joinToString(separator = "\n")
                )
            }
        }

        override fun onSelfNoted(syncNotedAppOp: SyncNotedAppOp) {
        }

        override fun onAsyncNoted(asyncNotedAppOp: AsyncNotedAppOp) {
            if (asyncNotedAppOp.attributionTag != null) {
                log(
                    asyncNotedAppOp.op,
                    asyncNotedAppOp.attributionTag,
                    asyncNotedAppOp.message,
                    asyncNotedAppOp.time.toString(),
                    asyncNotedAppOp.notingUid.toString()
                )
            } else {
                logE(
                    asyncNotedAppOp.op,
                    asyncNotedAppOp.message,
                    asyncNotedAppOp.time.toString(),
                    asyncNotedAppOp.notingUid.toString()
                )
            }
        }

        private fun log(vararg items: String?) {
            Log.d("data-audition", items.joinToString(separator = "\n"))
        }

        private fun logE(vararg items: String?) {
            Log.e("data-audition", items.joinToString(separator = "\n"))
        }
    }

    val appOpsManager = getSystemService(AppOpsManager::class.java) as AppOpsManager
    appOpsManager.setOnOpNotedCallback(mainExecutor, appOpsCallback)
}