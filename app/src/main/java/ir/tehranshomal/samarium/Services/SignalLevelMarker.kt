package ir.tehranshomal.samarium.Services

import ir.tehranshomal.samarium.R

fun getMarkerDrawable(x: Int): Int {
    val color = when (x) {
        in -80..Int.MAX_VALUE -> R.drawable.marker_excellent // Excellent
        in -85..-80 -> R.drawable.marker_very_good // Very Good
        in -90..-85 -> R.drawable.marker_good // Good
        in -95..-90 -> R.drawable.marker_fair // Fair
        in -100..-95 -> R.drawable.marker_poor // Poor
        in -105..-100 -> R.drawable.marker_very_poor // Very Poor
        in -110..-105 -> R.drawable.marker_bad // Bad
        in -115..-110 -> R.drawable.marker_very_bad // Very Bad
        in -120..-115 -> R.drawable.marker_awful // Awful
        else -> R.drawable.marker_no_coverage // No Coverage
    }
    return color
}