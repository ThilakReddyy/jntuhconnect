package com.dhethi.jntuhconnect.presentation.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.staticCompositionLocalOf
import com.dhethi.jntuhconnect.BuildConfig
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

data class AdsConsentState(
    val canRequestAds: Boolean = false,
    val privacyOptionsRequired: Boolean = false,
    val sdkInitialized: Boolean = false
) {
    val adsReady: Boolean get() = canRequestAds && sdkInitialized
}

val LocalAdsConsentManager = staticCompositionLocalOf<AdsConsentManager?> { null }

@Singleton
class AdsConsentManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val consentInformation = UserMessagingPlatform.getConsentInformation(context)
    private val _state = MutableStateFlow(AdsConsentState())
    val state = _state.asStateFlow()
    private val initializationStarted = AtomicBoolean(false)
    private val initializationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun gatherConsent(activity: Activity) {
        if (!BuildConfig.RESULTS_ADS_ENABLED) return
        consentInformation.requestConsentInfoUpdate(
            activity,
            ConsentRequestParameters.Builder().build(),
            {
                if (activity.isFinishing || activity.isDestroyed) return@requestConsentInfoUpdate
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { error ->
                    error?.let { Log.d("AdsConsent", "Consent form unavailable: ${it.errorCode}") }
                    updateConsentState()
                }
                updateConsentState()
            },
            { error ->
                Log.d("AdsConsent", "Consent update unavailable: ${error.errorCode}")
                // UMP can allow ads using valid consent from an earlier session.
                updateConsentState()
            }
        )
        updateConsentState()
    }

    // Called only after results are available; SDK work never gates result requests.
    fun initializeIfAllowed() {
        if (!BuildConfig.RESULTS_ADS_ENABLED || !consentInformation.canRequestAds()) return
        if (!initializationStarted.compareAndSet(false, true)) return
        initializationScope.launch {
            runCatching {
                MobileAds.initialize(context) {
                    _state.update { it.copy(sdkInitialized = true) }
                }
            }.onFailure {
                Log.d("AdsConsent", "Ads initialization unavailable")
            }
        }
    }

    fun showPrivacyOptions(activity: Activity) {
        // Removing any visible ad while the user changes consent avoids stale requests.
        _state.update { it.copy(canRequestAds = false) }
        UserMessagingPlatform.showPrivacyOptionsForm(activity) { error ->
            error?.let { Log.d("AdsConsent", "Privacy options unavailable: ${it.errorCode}") }
            updateConsentState()
        }
    }

    private fun updateConsentState() {
        _state.update {
            it.copy(
                canRequestAds = consentInformation.canRequestAds(),
                privacyOptionsRequired = consentInformation.privacyOptionsRequirementStatus ==
                    ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
            )
        }
    }
}
