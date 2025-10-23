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
import java.net.HttpURLConnection
import java.net.URL


fun openCustomTab(context: Context, url: String) {
    val customTabsIntent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .setInstantAppsEnabled(true)
        .build()

    customTabsIntent.launchUrl(context, url.toUri())
}

fun buildResultUrl(rollNumber: String, examResult: ExamResult): String {
    val degree = if (rollNumber[5] == 'R') "bpharmacy" else "btech"
    val resultType = if (examResult.rcrv) "gradercrv" else "null"
    val gradeType = if (examResult.rcrv) "rcrvintgrade" else "intgrade"

    return "http://results.jntuh.ac.in/results/resultAction?" +
            "degree=$degree&examCode=${examResult.examCode}&etype=r16" +
            "&result=$resultType&grad=null&type=$gradeType&htno=$rollNumber"
}


data class GradeColors(val textColor: Color, val backgroundColor: Color)

fun getGradeColors(grade: String): GradeColors {
    return when (grade) {
        "O"  -> GradeColors(Color(0xFF0F766E), Color(0xFFCCFBF1)) // teal, standout for Outstanding
        "A+" -> GradeColors(Color(0xFF15803D), Color(0xFFEFFDF4)) // text-green-700 / bg-green-50
        "A"  -> GradeColors(Color(0xFF16A34A), Color(0xFFEFFDF4)) // text-green-600 / bg-green-50
        "B+" -> GradeColors(Color(0xFF2563EB), Color(0xFFEFF6FF)) // text-blue-600 / bg-blue-50
        "B"  -> GradeColors(Color(0xFF3B82F6), Color(0xFFEFF6FF)) // text-blue-500 / bg-blue-50
        "C"  -> GradeColors(Color(0xFFCA8A04), Color(0xFFFEFCE8)) // text-yellow-600 / bg-yellow-50
        "D"  -> GradeColors(Color(0xFF9333EA), Color(0xFFF3E8FF)) // purple for borderline pass
        "F"  -> GradeColors(Color(0xFFDC2626), Color(0xFFFEF2F2)) // text-red-600 / bg-red-50
        else -> GradeColors(Color(0xFF4B5563), Color(0xFFF9FAFB)) // text-gray-600 / bg-gray-50
    }
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


fun isServerReachable( timeout: Int = 3000): Boolean {
    return try {
        val url = URL(Constants.BASE_URL+"health")
        val connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = timeout
        connection.requestMethod = "GET" // lightweight request

        connection.responseCode == HttpURLConnection.HTTP_OK
    } catch (e: Exception) {
        false
    }
}