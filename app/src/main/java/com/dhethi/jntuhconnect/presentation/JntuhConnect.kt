package com.dhethi.jntuhconnect.presentation

import android.app.Application
import com.dhethi.jntuhconnect.BuildConfig
import com.dhethi.jntuhconnect.telemetry.CrashlyticsTree
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class JntuhConnect : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else CrashlyticsTree())
    }
}
