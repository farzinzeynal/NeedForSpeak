package az.needforspeak.ui.unregister

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import az.needforspeak.R
import az.needforspeak.base.BaseFragment
import az.needforspeak.databinding.FragmentAddUserDataBinding
import az.needforspeak.utils.SharedTypes
import az.needforspeak.utils.getNavOptions
import az.needforspeak.utils.helpers.MainSharedPrefs
import az.needforspeak.view_model.LoginViewModel
import kotlinx.android.synthetic.main.fragment_registration.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddUserDataFragment :
    BaseFragment<FragmentAddUserDataBinding>(FragmentAddUserDataBinding::inflate),View.OnClickListener,TextWatcher {

    private val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initNewRegisterApi()



    }

    private fun initNewRegisterApi() {
        viewModel.registerNewUserResponse.observe(viewLifecycleOwner){
            findNavController().navigate(R.id.loginFragment, bundleOf("isFromRegister" to true), getNavOptions())
               MainSharedPrefs(requireContext(),SharedTypes.USERDATA).set("REG_ID",0)
           }
    }

    private fun initViews() {
        views.btnFinishRegister.setOnClickListener(this)
        views.txtPassword.addTextChangedListener(this)
        views.txtPasswordConfirm.addTextChangedListener(this)
    }


    override fun onClick(p0: View?) {
        when(p0?.id){

            R.id.btnFinishRegister ->{
                try{
                    val reqID = MainSharedPrefs(requireContext(),SharedTypes.USERDATA).get("REG_ID",0) ?: 0
                    viewModel.registerNewUser(
                        regId = reqID,
                        name = views.txtName.text.toString(),
                        sureName = views.txtsureName.text.toString(),
                        password = views.txtPassword.text.toString()
                    )
                }
                catch (ex: Exception){
                    ex
                }

            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (views.txtPassword.text.toString()==views.txtPasswordConfirm.text.toString()) {
            views.registerTitle.visibility = View.GONE
        }
        else {
            views.registerTitle.visibility = View.VISIBLE
        }
    }

    override fun afterTextChanged(p0: Editable?) {

    }

}