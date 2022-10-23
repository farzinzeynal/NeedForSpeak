package az.needforspeak.utils

import androidx.annotation.IntDef

object UserType {
    //Constants
    const val TYPE_FRIEND = 1
    const val TYPE_NORMAL = 2
    const val TYPE_OWN = 3

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(TYPE_FRIEND, TYPE_NORMAL, TYPE_OWN)
    annotation class UserTypeDef
}