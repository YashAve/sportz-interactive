package com.performance.sportzinteractive.ui

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.performance.sportzinteractive.R

object GoogleFonts {

    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    val kalam = GoogleFont(name = "kalam")
    val kalamFamily = FontFamily(
        Font(googleFont = kalam, fontProvider = provider, weight = FontWeight.Bold, style = FontStyle.Normal)
    )
}