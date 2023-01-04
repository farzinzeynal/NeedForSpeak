package az.needforspeak.utils.type_defs

import androidx.annotation.IntDef

object FriendListType {
    //Constants
    const val TYPE_LETTER = 1
    const val TYPE_FRIEND = 2

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(TYPE_LETTER, TYPE_FRIEND)
    annotation class FriendListTypeDef
}