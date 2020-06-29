package by.android.academy.privacy

import android.annotation.TargetApi
import android.app.AppOpsManager
import android.app.Application
import android.app.AsyncNotedAppOp
import android.app.SyncNotedAppOp
import android.os.Build
import android.util.Log

class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            setupDataAudition()
        }
    }

    @TargetApi(Build.VERSION_CODES.R)
    private fun setupDataAudition() {
        val appOpsCallback = object : AppOpsManager.OnOpNotedCallback() {
            private fun logPrivateDataAccess(opCode: String, trace: String) {
                Log.i(
                    "data-audition",
                    "Private data accessed. Operation: $opCode\nStack Trace:\n$trace"
                )
            }

            override fun onNoted(syncNotedAppOp: SyncNotedAppOp) {
                logPrivateDataAccess(
                    syncNotedAppOp.op, getStackTrace()
                )
            }

            override fun onSelfNoted(syncNotedAppOp: SyncNotedAppOp) {
                logPrivateDataAccess(
                    syncNotedAppOp.op, getStackTrace()
                )
            }

            override fun onAsyncNoted(asyncNotedAppOp: AsyncNotedAppOp) {
                logPrivateDataAccess(asyncNotedAppOp.op, asyncNotedAppOp.message)
            }
        }

        val appOpsManager = getSystemService(AppOpsManager::class.java) as AppOpsManager
        appOpsManager.setOnOpNotedCallback(mainExecutor, appOpsCallback)
    }

    private inline fun getStackTrace(): String {
        return Throwable().stackTrace.toList().toString()
    }
}