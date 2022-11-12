package az.needforspeak.ui.register.settings

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import az.needforspeak.R
import az.needforspeak.base.BaseFragment
import az.needforspeak.component.adapter.MarketAdapter
import az.needforspeak.component.adapter.SettingsListAdapter
import az.needforspeak.databinding.FragmentUserSettingsBinding
import az.needforspeak.model.local.SettingsModel
import az.needforspeak.utils.getNavOptions

class UserSettingsFragment: BaseFragment<FragmentUserSettingsBinding>(FragmentUserSettingsBinding::inflate) {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: SettingsListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutManager = LinearLayoutManager(requireContext())

        val mSettingsList = mutableListOf<SettingsModel>()
        mSettingsList.add(SettingsModel(1,"Saved messages and posts", R.drawable.ic_car_key,R.id.savedMessagesFragment))
        mSettingsList.add(SettingsModel(2,"Change your car", R.drawable.ic_car_key,R.id.changeYourCarFragment))
        mSettingsList.add(SettingsModel(3,"Notifications and alerts", R.drawable.ic_car_key,R.id.notificationsAndAlertsFragment))
        mSettingsList.add(SettingsModel(4,"Language", R.drawable.ic_car_key,R.id.languagesFragment))
        mSettingsList.add(SettingsModel(5,"Support", R.drawable.ic_car_key,R.id.supportFragment))
        mSettingsList.add(SettingsModel(6,"Log out", R.drawable.ic_car_key,null))
        mSettingsList.add(SettingsModel(7,"Delete your account", R.drawable.ic_car_key,null))


        adapter = SettingsListAdapter(requireContext()){
            it.destionationFragment?.let { it1 -> findNavController().navigate(it1, null, getNavOptions()) }
        }

        adapter.addData(mSettingsList)


        views.settingsList.layoutManager = linearLayoutManager
        views.settingsList.adapter = adapter


    }
}