package com.dhethi.jntuhconnect.presentation.ads

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dhethi.jntuhconnect.BuildConfig
import com.dhethi.jntuhconnect.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.AdChoicesView
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.delay

private const val AD_CACHE_LIFETIME_MS = 60 * 60 * 1000L

/** Screen ownership keeps lazy-list recycling and tab switches from requesting new ads. */
class ResultsNativeAdState(private val context: Context) {
    var adView by mutableStateOf<NativeAdView?>(null)
        private set
    var resumed by mutableStateOf(false)
        private set
    private var nativeAd: NativeAd? = null
    private var requested = false
    private var disposed = false
    private var generation = 0
    private var loadedAt = 0L
    private var attached = false

    fun load() {
        if (disposed || !resumed) return
        if (nativeAd != null && SystemClock.elapsedRealtime() - loadedAt >= AD_CACHE_LIFETIME_MS) {
            clear()
        }
        if (requested) return
        requested = true
        val requestGeneration = generation
        AdLoader.Builder(context, BuildConfig.ADMOB_RESULTS_NATIVE_ID)
            .forNativeAd { ad ->
                // Consent or the screen can disappear while the SDK request is in flight.
                if (disposed || requestGeneration != generation) {
                    ad.destroy()
                } else {
                    nativeAd = ad
                    loadedAt = SystemClock.elapsedRealtime()
                    adView = createNativeAdView(context, ad)
                    updatePlayback(wasActive = false)
                }
            }
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                    .setVideoOptions(VideoOptions.Builder().setStartMuted(true).build())
                    .build()
            )
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    // Keep results usable on no-fill/network errors; do not retry in a loop.
                }
            })
            .build()
            // No student identifiers, grades, or other targeting extras are passed to ads.
            .loadAd(AdRequest.Builder().build())
    }

    fun setAttached(value: Boolean) {
        val wasActive = resumed && attached
        attached = value
        updatePlayback(wasActive)
    }

    fun updateLifecycle(value: Boolean) {
        val wasActive = resumed && attached
        resumed = value
        updatePlayback(wasActive)
    }

    private fun updatePlayback(wasActive: Boolean) {
        val active = resumed && attached
        val controller = nativeAd?.mediaContent?.videoController ?: return
        if (!active) controller.pause() else if (!wasActive) controller.play()
    }

    fun clear() {
        generation++
        adView?.let { view ->
            (view.parent as? ViewGroup)?.removeView(view)
            view.destroy()
        }
        adView = null
        nativeAd?.destroy()
        nativeAd = null
        requested = false
        attached = false
    }

    fun dispose() {
        disposed = true
        clear()
    }
}

@Composable
fun rememberResultsNativeAdState(): ResultsNativeAdState {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state = remember(context, lifecycle) { ResultsNativeAdState(context) }
    val consentManager = LocalAdsConsentManager.current
    val consent = consentManager?.state?.collectAsState()?.value
    // Observe consent at screen scope, including while the ad is scrolled off screen.
    LaunchedEffect(state, consent?.canRequestAds) {
        if (consent?.canRequestAds != true) state.clear()
    }
    DisposableEffect(state, lifecycle) {
        val observer = LifecycleEventObserver { _, _ ->
            state.updateLifecycle(lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED))
        }
        lifecycle.addObserver(observer)
        state.updateLifecycle(lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED))
        onDispose {
            lifecycle.removeObserver(observer)
            state.dispose()
        }
    }
    return state
}

@Composable
fun ResultsNativeAd(state: ResultsNativeAdState, modifier: Modifier = Modifier) {
    val consentManager = LocalAdsConsentManager.current ?: return
    if (!BuildConfig.RESULTS_ADS_ENABLED) return
    val consent by consentManager.state.collectAsState()
    LaunchedEffect(state, consent.adsReady, consent.canRequestAds, state.resumed) {
        if (consent.canRequestAds) consentManager.initializeIfAllowed()
        if (consent.adsReady && state.resumed) {
            delay(600)
            state.load()
        }
    }
    if (!consent.adsReady) return
    val view = state.adView ?: return
    val colors = MaterialTheme.colorScheme
    AndroidView(
        factory = { context -> FrameLayout(context) },
        update = { container ->
            view.applyColors(colors)
            if (view.parent !== container) {
                container.removeAllViews()
                (view.parent as? ViewGroup)?.removeView(view)
                container.addView(view)
            }
            state.setAttached(true)
        },
        onRelease = { container ->
            container.removeAllViews()
            state.setAttached(false)
        },
        // This is the last list item: loading it cannot move result rows or controls.
        modifier = modifier.fillMaxWidth().padding(vertical = 16.dp)
    )
}

private fun createNativeAdView(context: Context, ad: NativeAd): NativeAdView {
    val view = LayoutInflater.from(context)
        .inflate(R.layout.results_native_ad, FrameLayout(context), false) as NativeAdView
    view.adChoicesView = view.findViewById<AdChoicesView>(R.id.ad_choices)
    view.headlineView = view.findViewById<TextView>(R.id.ad_headline).apply { text = ad.headline }
    view.bodyView = view.findViewById<TextView>(R.id.ad_body).apply {
        text = ad.body
        visibility = if (ad.body.isNullOrBlank()) View.GONE else View.VISIBLE
    }
    view.advertiserView = view.findViewById<TextView>(R.id.ad_advertiser).apply {
        text = ad.advertiser
        visibility = if (ad.advertiser.isNullOrBlank()) View.GONE else View.VISIBLE
    }
    view.iconView = view.findViewById<ImageView>(R.id.ad_icon).apply {
        setImageDrawable(ad.icon?.drawable)
        visibility = if (ad.icon == null) View.GONE else View.VISIBLE
    }
    view.callToActionView = view.findViewById<Button>(R.id.ad_call_to_action).apply {
        text = ad.callToAction
        visibility = if (ad.callToAction.isNullOrBlank()) View.GONE else View.VISIBLE
        // The SDK owns all click handling for registered native ad assets.
    }
    view.mediaView = view.findViewById<MediaView>(R.id.ad_media).apply {
        mediaContent = ad.mediaContent
        setImageScaleType(ImageView.ScaleType.FIT_CENTER)
        visibility = if (ad.mediaContent == null) View.GONE else View.VISIBLE
    }
    view.setNativeAd(ad)
    return view
}

private fun NativeAdView.applyColors(colors: ColorScheme) {
    val density = resources.displayMetrics.density
    background = GradientDrawable().apply {
        cornerRadius = 16 * density
        setColor(colors.surfaceContainer.toArgb())
        setStroke(density.toInt().coerceAtLeast(1), colors.outlineVariant.toArgb())
    }
    findViewById<TextView>(R.id.ad_headline).setTextColor(colors.onSurface.toArgb())
    findViewById<TextView>(R.id.ad_body).setTextColor(colors.onSurfaceVariant.toArgb())
    findViewById<TextView>(R.id.ad_advertiser).setTextColor(colors.onSurfaceVariant.toArgb())
    findViewById<TextView>(R.id.ad_attribution).apply {
        setTextColor(colors.onSecondaryContainer.toArgb())
        setBackgroundColor(colors.secondaryContainer.toArgb())
    }
    findViewById<Button>(R.id.ad_call_to_action).apply {
        setTextColor(colors.onPrimary.toArgb())
        backgroundTintList = ColorStateList.valueOf(colors.primary.toArgb())
    }
}
