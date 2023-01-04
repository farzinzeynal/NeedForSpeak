package az.needforspeak.ui.register

import android.os.Bundle
import android.util.Log
import android.view.View
import az.needforspeak.R
import az.needforspeak.base.BaseActivity
import az.needforspeak.base.BaseFragment
import az.needforspeak.databinding.FragmentAccountBinding
import az.needforspeak.model.remote.auth.ProfileResponseModel
import az.needforspeak.utils.Extentions.fromStringToJid
import az.needforspeak.utils.SessionManager
import az.needforspeak.utils.fromJidToString
import az.needforspeak.view_model.AccountViewModel
import az.needforspeak.view_model.FriendsViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.account_info_item.view.*
import kotlinx.android.synthetic.main.fragment_account.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : BaseFragment<FragmentAccountBinding>(FragmentAccountBinding::inflate) {

    private val viewModel: AccountViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initProfileApi()

        viewModel.getUserProfileData("10-DD-555")

        views.plateLayout.plateNum.text = "10-AA-001"


    }

    private fun initProfileApi() {
        viewModel.profileResponse.observe(viewLifecycleOwner) {
            BaseActivity.loadingDown()
            if(it.data!=null){
                setDatas(it.data)
            }
        }
    }

    private fun setDatas(data: ProfileResponseModel) {
        views.inputName.setText(data.name?.value)
        views.inputSurename.setText(data.surname?.value)
        views.inputPhoneNumber.setText(data.phone_number?.value)
        views.inputStatus.setText(data.about?.value)
        views.inputCareer.setText(data.career?.value)
        views.inputEducation.setText(data.education?.value)
        views.inputInterestes.setText(data.interests?.value)

        views.plateLayout.plateNum.text = SessionManager.getCurrentUserId()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            requireActivity().window.statusBarColor =
                resources.getColor(R.color.background_two_color)
        }
    }

    override fun onPause() {
        super.onPause()
    }
}