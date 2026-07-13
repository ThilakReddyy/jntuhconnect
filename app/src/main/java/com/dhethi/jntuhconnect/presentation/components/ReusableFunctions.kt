package com.dhethi.jntuhconnect.presentation.components

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.Color
import androidx.core.net.toUri
import com.dhethi.jntuhconnect.common.Constants
import com.dhethi.jntuhconnect.domain.model.ExamResult
import com.dhethi.jntuhconnect.presentation.theme.gradeAverage
import com.dhethi.jntuhconnect.presentation.theme.gradeBorderline
import com.dhethi.jntuhconnect.presentation.theme.gradeExcellent
import com.dhethi.jntuhconnect.presentation.theme.gradeFail
import com.dhethi.jntuhconnect.presentation.theme.gradeGood
import com.dhethi.jntuhconnect.presentation.theme.gradeOutstanding
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL


fun isSafeWebUrl(url: String): Boolean = runCatching {
    val uri = URI(url.trim())
    uri.scheme?.lowercase() in setOf("http", "https") && !uri.host.isNullOrBlank()
}.getOrDefault(false)

fun openCustomTab(context: Context, url: String): Boolean {
    if (!isSafeWebUrl(url)) return false
    val customTabsIntent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .setInstantAppsEnabled(true)
        .build()

    return runCatching {
        customTabsIntent.launchUrl(context, url.trim().toUri())
        true
    }.getOrDefault(false)
}

fun buildResultUrl(rollNumber: String, examResult: ExamResult): String {
    val degree = if (rollNumber.getOrNull(5) == 'R') "bpharmacy" else "btech"
    val resultType = if (examResult.rcrv) "gradercrv" else "null"
    val gradeType = if (examResult.rcrv) "rcrvintgrade" else "intgrade"

    return "http://results.jntuh.ac.in/results/resultAction?" +
            "degree=$degree&examCode=${examResult.examCode}&etype=r16" +
            "&result=$resultType&grad=null&type=$gradeType&htno=$rollNumber"
}


/** Semantic tint for a grade letter; readable on both light and dark surfaces. */
fun gradeColor(grade: String): Color = when (grade.uppercase()) {
    "O" -> gradeOutstanding
    "A+", "A" -> gradeExcellent
    "B+", "B" -> gradeGood
    "C" -> gradeAverage
    "D" -> gradeBorderline
    "F" -> gradeFail
    else -> gradeGood
}



@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}


fun isServerReachable(timeout: Int = 3000): Boolean {
    val connection = try {
        URL(Constants.BASE_URL + "health").openConnection() as HttpURLConnection
    } catch (e: Exception) {
        return false
    }
    return try {
        connection.connectTimeout = timeout
        connection.readTimeout = timeout
        connection.requestMethod = "GET"

        connection.responseCode == HttpURLConnection.HTTP_OK
    } catch (e: Exception) {
        false
    } finally {
        connection.disconnect()
    }
}
