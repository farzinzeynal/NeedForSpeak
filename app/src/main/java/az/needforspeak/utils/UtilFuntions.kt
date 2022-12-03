package az.needforspeak.utils

import android.content.Context
import android.util.DisplayMetrics
import kotlin.math.roundToInt

object UtilFuntions {

    fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    fun pxToDp(px: Float, context: Context): Float {
        return (px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))
    }

}