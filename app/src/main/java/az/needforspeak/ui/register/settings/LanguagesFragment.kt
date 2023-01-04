package az.needforspeak.ui.register.settings

import android.os.Bundle
import android.view.View
import az.needforspeak.base.BaseFragment
import az.needforspeak.databinding.FragmentLanguageBinding
import az.needforspeak.utils.helpers.LocaleHelper
import az.needforspeak.utils.type_defs.SelectedLanguage

class LanguagesFragment : BaseFragment<FragmentLanguageBinding>(FragmentLanguageBinding::inflate),View.OnClickListener{

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSelection(LocaleHelper.getLanguage(requireContext()) ?: SelectedLanguage.AZ)
         views.az.setOnClick(this)
         views.en.setOnClick(this)
         views.ru.setOnClick(this)
    }

    private fun setSelectedLanguage(@SelectedLanguage.LanguageDef languageId: String) {
        setSelection(languageId)
        changeLanguage(languageId)
    }

    private fun setSelection(@SelectedLanguage.LanguageDef languageId: String) {
        views.az.setIconVisibility(if (languageId == SelectedLanguage.AZ) View.VISIBLE else View.GONE)
        views.en.setIconVisibility(if (languageId == SelectedLanguage.EN) View.VISIBLE else View.GONE)
        views.ru.setIconVisibility(if (languageId == SelectedLanguage.RU) View.VISIBLE else View.GONE)
    }


    private fun changeLanguage(@SelectedLanguage.LanguageDef languageId: String) {
        LocaleHelper.setLocale(requireContext(), languageId)
      //  activity?.recreate()
    }

    override fun onClick(view: View?) {
        when (view) {
            views.az -> setSelectedLanguage(SelectedLanguage.AZ)
            views.en -> setSelectedLanguage(SelectedLanguage.EN)
            views.ru -> setSelectedLanguage(SelectedLanguage.RU)
        }
    }
}
