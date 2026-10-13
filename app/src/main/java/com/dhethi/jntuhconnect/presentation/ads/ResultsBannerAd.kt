package com.dhethi.jntuhconnect.presentation.ads

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dhethi.jntuhconnect.BuildConfig
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import kotlinx.coroutines.delay

private const val MAX_BANNER_HEIGHT_DP = 60

/** Owned by the screen so scrolling and switching tabs reuse the same request. */
class ResultsBannerState(private val context: Context) {
    var adView by mutableStateOf<AdView?>(null)
        private set
    var loaded by mutableStateOf(false)
        private set
    var resumed by mutableStateOf(false)
        private set
    private var requestedWidth = 0
    private var attached = false

    fun load(widthDp: Int) {
        if (widthDp < 1 || widthDp == requestedWidth || !resumed) return
        destroy()
        requestedWidth = widthDp
        adView = AdView(context).apply {
            adUnitId = BuildConfig.ADMOB_RESULTS_BANNER_ID
            setAdSize(AdSize.getInlineAdaptiveBannerAdSize(widthDp, MAX_BANNER_HEIGHT_DP))
            adListener = object : AdListener() {
                override fun onAdLoaded() { loaded = true }
                override fun onAdFailedToLoad(error: LoadAdError) { loaded = false }
            }
            // Only SDK defaults are sent: no roll number, grades or student details.
            loadAd(AdRequest.Builder().build())
        }
    }

    fun setAttached(value: Boolean) {
        attached = value
        updatePlayback()
    }

    fun updateLifecycle(value: Boolean) {
        resumed = value
        updatePlayback()
    }

    private fun updatePlayback() {
        if (resumed && attached) adView?.resume() else adView?.pause()
    }

    fun destroy() {
        adView?.let { view ->
            view.adListener = object : AdListener() {}
            (view.parent as? ViewGroup)?.removeView(view)
            view.destroy()
        }
        adView = null
        loaded = false
        requestedWidth = 0
    }
}

@Composable
fun rememberResultsBannerState(): ResultsBannerState {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val banner = remember(context) { ResultsBannerState(context) }
    DisposableEffect(banner, lifecycle) {
        val observer = LifecycleEventObserver { _, _ ->
            banner.updateLifecycle(lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED))
        }
        lifecycle.addObserver(observer)
        banner.updateLifecycle(lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED))
        onDispose {
            lifecycle.removeObserver(observer)
            banner.destroy()
        }
    }
    return banner
}

@Composable
fun ResultsBannerAd(banner: ResultsBannerState, modifier: Modifier = Modifier) {
    val consentManager = LocalAdsConsentManager.current ?: return
    if (!BuildConfig.RESULTS_ADS_ENABLED) return
    val consent by consentManager.state.collectAsState()

    // A compact fixed slot keeps result rows still while an ad loads or fails.
    // The slot sits after content and leaves a gap from nearby result controls.
    BoxWithConstraints(modifier.fillMaxWidth().padding(vertical = 16.dp)) {
        val widthDp = maxWidth.value.toInt()
        LaunchedEffect(widthDp, consent.adsReady, consent.canRequestAds, banner.resumed) {
            if (consent.canRequestAds) consentManager.initializeIfAllowed()
            if (consent.adsReady && banner.resumed) {
                delay(600) // Let the result content settle before requesting the banner.
                banner.load(widthDp)
            }
        }
        LaunchedEffect(consent.canRequestAds) {
            if (!consent.canRequestAds) banner.destroy()
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.fillMaxWidth().height(20.dp), contentAlignment = Alignment.Center) {
                if (banner.loaded) {
                    Text(
                        "Ad",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Box(
                Modifier.fillMaxWidth().height(MAX_BANNER_HEIGHT_DP.dp),
                contentAlignment = Alignment.Center
            ) {
                banner.adView?.let { view ->
                    AndroidView(
                        factory = { context ->
                            FrameLayout(context).apply {
                                (view.parent as? ViewGroup)?.removeView(view)
                                addView(view)
                                banner.setAttached(true)
                            }
                        },
                        update = { container ->
                            if (view.parent !== container) {
                                container.removeAllViews()
                                (view.parent as? ViewGroup)?.removeView(view)
                                container.addView(view)
                                banner.setAttached(true)
                            }
                        },
                        onRelease = { container ->
                            container.removeAllViews()
                            banner.setAttached(false)
                        },
                        modifier = Modifier.fillMaxWidth().alpha(if (banner.loaded) 1f else 0f)
                    )
                }
            }
        }
    }
}
