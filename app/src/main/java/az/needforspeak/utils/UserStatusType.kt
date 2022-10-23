package az.needforspeak.utils

import androidx.annotation.IntDef

object UserStatusType {
    //Constants
    const val AVAILABLE = 1
    const val UNAVAILABLE = 2

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(AVAILABLE, UNAVAILABLE)
    annotation class UserStatusTypeDef
}