package az.needforspeak.ui.unregister

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import az.needforspeak.R
import az.needforspeak.base.BaseFragment
import az.needforspeak.databinding.FragmentCheckRequestBinding
import az.needforspeak.utils.SharedTypes
import az.needforspeak.utils.getNavOptions
import az.needforspeak.utils.helpers.MainSharedPrefs
import az.needforspeak.view_model.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckRegisterFragment : BaseFragment<FragmentCheckRequestBinding>(FragmentCheckRequestBinding::inflate), View.OnClickListener {

    var regId =0
    private val viewModel: LoginViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initVerifyApi()
        regId = MainSharedPrefs(requireContext(),SharedTypes.USERDATA).get("REG_ID",0) ?: 0
        arguments?.let {

            val isSuccess = it.getBoolean("isSuccess")
            val isFromDeepLink = it.getBoolean("isFromDeepLink")
            if (regId!=0){
                initRegStatusApi()
                viewModel.checkRegistrationStatus(regId)
            }
            if (isSuccess){
                showSuccessView()
            }
            if (isFromDeepLink){
                showOtpView(regId)
            }
        }


    }

    private fun initViews() {
        views.verifyCode.setOnClickListener(this)
        views.btnContinue.setOnClickListener(this)
        views.getNewCode.setOnClickListener(this)
    }

    private fun initVerifyApi() {
        viewModel.verifyOtpResponse.observe(viewLifecycleOwner){
            if (it.data!=null){
                showSuccessView()
            }
        }
    }

    private fun showOtpView(regId: Int) {
        views.pendingText.text = ""
        views.otpLayout.visibility = View.VISIBLE
        viewModel.sendOtpRequest(regId)
    }

    private fun showSuccessView() {
        views.sucessMessage.text = ""
        views.otpLayout.visibility = View.GONE
        views.successLayout.visibility = View.VISIBLE
        arguments?.remove("isFromDeepLink")
    }

    private fun initRegStatusApi() {
        viewModel.regStatusResponse.observe(viewLifecycleOwner){
            if (it.data!=null){
                if (it.data.is_approved==0 || it.data.is_approved==3){
                    views.pendingText.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.verifyCode ->{
                viewModel.verifyCode(regId,views.enteredOtpCode.text.toString())
            }

            R.id.btnContinue ->{
               findNavController().navigate(R.id.addUserDataFragment,null, getNavOptions())
            }

            R.id.getNewCode ->{
                viewModel.sendOtpRequest(regId)
            }

        }
    }


}