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

    fun getCurrentTimeStamp(): Long {
        return System.currentTimeMillis()
    }

    // phoneFormatter
    fun formatterPaymentPhone(item: String, type: Int): String {
        val st = item.replace("\\s".toRegex(), "") //delete whitespaces
        val z = st.toCharArray() //to char aarray
        var res: String = ""
        val temp: CharArray = CharArray(13)

        try {
            for (t in z.indices) {
                when {
                    t < type -> temp[t] = z[t]
                    t in type..type+2 -> {
                        if (t == 3) temp[t + 1] = ' '
                        temp[t + 1] = z[t]
                    }
                    t in type+3..type+4 -> {
                        if (t == 7) temp[t + 2] = ' '
                        temp[t + 2] = z[t]
                    }

                    t in type+5..type+8 -> {
                        if (t == 10) temp[t + 3] = ' '
                        temp[t + 3] = z[t]
                    }
                    else -> {
                    }
                }
            }
            //build result string
            for (o in temp) {
                res += if (Character.isDigit(o)) {
                    o
                } else {
                    " "
                }
            }
        } catch (ex: Exception) {
            res = item
        }

        return res.trim()
    }

}