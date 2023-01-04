package az.needforspeak.utils.helpers

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import az.needforspeak.utils.SharedConstants
import az.needforspeak.utils.SharedTypes
import az.needforspeak.utils.isVersionHigherAndEqual
import az.needforspeak.utils.type_defs.SelectedLanguage
import java.util.*

object LocaleHelper {


    fun onAttach(context: Context): Context {
        val lang = getPersistedData(context, Locale.getDefault().language)
        return setLocale(context, lang)
    }

    fun onAttach(context: Context, defaultLanguage: String): Context {
        val lang = getPersistedData(context, defaultLanguage)
        return setLocale(context, lang)
    }

    fun getLanguage(context: Context): String? {
        return getPersistedData(context, Locale.getDefault().language)
    }

    fun setLocale(context: Context, language: String?): Context {
        MainSharedPrefs(context, SharedTypes.SETTINGS).set(SharedConstants.LANGUAGE, language)
        return if (isVersionHigherAndEqual(Build.VERSION_CODES.N)) {
            updateResources(context, language)
        } else updateResourcesLegacy(context, language)

    }

    private fun getPersistedData(context: Context, defaultLanguage: String): String? {
        return MainSharedPrefs(context, SharedTypes.SETTINGS).get(SharedConstants.LANGUAGE, defaultLanguage)
    }

    private fun persist(context: Context, @SelectedLanguage.LanguageDef languageId: Int) {
        MainSharedPrefs(context, SharedTypes.SETTINGS).set(SharedConstants.LANGUAGE, languageId)
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String?): Context {
        val locale = Locale(language!!)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context, language: String?): Context {
        val locale = Locale(language!!)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        if (isVersionHigherAndEqual(Build.VERSION_CODES.KITKAT)) {
            configuration.setLayoutDirection(locale)
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}