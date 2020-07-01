package by.android.academy.privacy

import android.app.Application

class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupDataAuditionCompat()
    }
}