package com.dhethi.jntuhconnect.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JntuhConnect : Application() {
    companion object {
        const val RESULT_NOTIFICATIONS_TOPIC = "result-updates"
    }
}
