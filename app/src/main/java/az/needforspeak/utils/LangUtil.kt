package az.needforspeak.utils

import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import java.util.*


fun setLocale(activity: Activity, languageCode: String?) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val resources: Resources = activity.resources
    val config: Configuration = resources.getConfiguration()
    config.setLocale(locale)
    resources.updateConfiguration(config, resources.getDisplayMetrics())
}

object StaticValues{
    var lang = "en"
}
