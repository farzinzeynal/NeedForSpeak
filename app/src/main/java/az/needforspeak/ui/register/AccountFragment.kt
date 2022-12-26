package az.needforspeak.ui.register

import android.os.Bundle
import android.view.View
import az.needforspeak.R
import az.needforspeak.base.BaseFragment
import az.needforspeak.databinding.FragmentAccountBinding
import az.needforspeak.utils.Extentions.fromStringToJid
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

       //
        userImage.setOnClickListener {
           // viewModel.getUserDataByUserId("10-FF-333".fromStringToJid())
        }

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser) {
            requireActivity().window.statusBarColor = resources.getColor(R.color.background_two_color)
        }
    }
}