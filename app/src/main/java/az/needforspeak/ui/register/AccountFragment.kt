package az.needforspeak.ui.register

import android.os.Bundle
import android.util.Log
import android.view.View
import az.needforspeak.R
import az.needforspeak.base.BaseFragment
import az.needforspeak.databinding.FragmentAccountBinding
import az.needforspeak.utils.Extentions.fromStringToJid
import az.needforspeak.utils.SessionManager
import az.needforspeak.utils.fromJidToString
import az.needforspeak.view_model.AccountViewModel
import az.needforspeak.view_model.FriendsViewModel
import kotlinx.android.synthetic.main.fragment_account.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : BaseFragment<FragmentAccountBinding>(FragmentAccountBinding::inflate) {

    private val viewModel: AccountViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = SessionManager.getCurrentUserJID()
        user

        views.userImage.setOnClickListener {
            viewModel.getUserDataByUserId(SessionManager.getCurrentUser())
        }

        views.plateLayout.plateNum.text = "10-AA-001"

        views.inputName.setText("Farzin")
        views.inputSurename.setText("Zeynalli")
        views.inputPhoneNumber.setText("0507705818")
        views.inputStatus.setText("My Status")
        views.inputCareer.setText("My Career")
        views.inputEducation.setText("My Education")
        views.inputInterestes.setText("My Interestes")
        views.inputPhoneNumber.setInputEnabled(false)



    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser) {
            requireActivity().window.statusBarColor = resources.getColor(R.color.background_two_color)
        }
    }
}