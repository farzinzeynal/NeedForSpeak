package az.needforspeak.utils

import androidx.annotation.StringDef

object SharedTypes {
    const val SETTINGS = "SETTINGS"
    const val REGISTRATION = "REGISTRATION"
    const val USERDATA = "USERDATA"
    @Retention(AnnotationRetention.SOURCE)
    @StringDef(SETTINGS, REGISTRATION, USERDATA)
    annotation class SharedTypesDef
}
