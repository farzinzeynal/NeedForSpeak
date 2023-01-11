package az.needforspeak.ui.register.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import az.needforspeak.R
import az.needforspeak.base.BaseFragment
import az.needforspeak.component.adapter.SettingsListAdapter
import az.needforspeak.databinding.FragmentUserSettingsBinding
import az.needforspeak.model.local.SettingsModel
import az.needforspeak.ui.unregister.UnregisterActivity
import az.needforspeak.utils.getNavOptions
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class UserSettingsFragment: BaseFragment<FragmentUserSettingsBinding>(FragmentUserSettingsBinding::inflate) {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: SettingsListAdapter
    val sharedPreferences by inject<SharedPreferences> { parametersOf("secure") }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutManager = LinearLayoutManager(requireContext())

        val mSettingsList = mutableListOf<SettingsModel>()
        mSettingsList.add(SettingsModel(1,"Saved messages and posts", R.drawable.ic_settings_saved_messages,R.id.savedMessagesFragment))
        mSettingsList.add(SettingsModel(2,"Change your car", R.drawable.ic_settings_change_auto,R.id.changeYourCarFragment))
        mSettingsList.add(SettingsModel(3,"Notifications and alerts", R.drawable.ic_settings_notification,R.id.notificationsAndAlertsFragment))
        mSettingsList.add(SettingsModel(4,"Language", R.drawable.ic_settings_language,R.id.languagesFragment))
        mSettingsList.add(SettingsModel(5,"Support", R.drawable.ic_settings_support,R.id.supportFragment))
        mSettingsList.add(SettingsModel(6,"Log out", null,null))
        mSettingsList.add(SettingsModel(7,"Delete your account", R.drawable.ic_settings_delete_account,null))


        adapter = SettingsListAdapter(requireContext()){
            it.destionationFragment?.let { it1 -> findNavController().navigate(it1, null, getNavOptions()) }
                ?: run{
                    when(it.id){
                        6 -> logoutFromAcc()
                        7 -> deleteProfile()
                    }
                }
        }

        adapter.addData(mSettingsList)


        views.settingsList.layoutManager = linearLayoutManager
        views.settingsList.adapter = adapter

    }

    private fun deleteProfile() {

    }

    private fun logoutFromAcc() {
        sharedPreferences.edit().putBoolean("isLogin", false).apply()
        sharedPreferences.edit().putString("username", "").apply()
        sharedPreferences.edit().putString("password", "").apply()
        requireActivity().startActivity(Intent(requireActivity(), UnregisterActivity::class.java))
        requireActivity().finish()
    }
}