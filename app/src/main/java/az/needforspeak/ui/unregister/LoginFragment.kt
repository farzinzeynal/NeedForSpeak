package az.needforspeak.ui.unregister

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.doOnTextChanged
import az.needforspeak.R
import az.needforspeak.base.BaseActivity
import az.needforspeak.base.BaseFragment
import az.needforspeak.databinding.FragmentLoginBinding
import az.needforspeak.ui.MainActivity
import az.needforspeak.utils.MaskFormatter
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    val sharedPreferences by inject<SharedPreferences> { parametersOf("secure") }
    private val viewModel: LoginViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        views.plateInclude.plateEditText.addTextChangedListener(MaskFormatter("99_AA_999", views.plateInclude.plateEditText))
        views.passwordEdittext.doOnTextChanged { text, start, before, count ->
            checkButton()
        }

        views.plateInclude.plateEditText.doOnTextChanged { text, start, before, count ->
            text?.let {
                if(it?.length == 9) {
                    BaseActivity.loadingUp()
                    viewModel.profile(it.toString())
                }else {
                    views.loginLayout.visibility = View.GONE
                }
            }
            checkButton()
        }

        viewModel.profileResponse.observe(viewLifecycleOwner) {
            BaseActivity.loadingDown()
            it.data?.let {
                views.loginLayout.visibility = View.VISIBLE
                sharedPreferences.edit().putString("profile", Gson().toJson(it)).commit()
            }
        }

        viewModel.successLogin.observe(viewLifecycleOwner) {
            if(it) {
                sharedPreferences.edit().putBoolean("isLogin", true).commit()
                sharedPreferences.edit().putString("username", views.plateInclude.plateEditText.text.toString()).commit()
                sharedPreferences.edit().putString("password", views.passwordEdittext.text.toString()).commit()
                requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
            }
        }

        views.loginButton.setOnClickListener {
            viewModel.login(requireContext(), views.plateInclude.plateEditText.text.toString(), views.passwordEdittext.text.toString())

        }

//        if(views.plateInclude.plateEditText.text.isEmpty()) {
//            views.plateInclude.plateEditText.inputType = InputType.TYPE_CLASS_NUMBER
//        }
        views.plateInclude.plateEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (views.plateInclude.plateEditText.text.length + 1 === 3 || views.plateInclude.plateEditText.text.length + 1 === 6) {
                    if (before - count < 0) {
                        views.plateInclude.plateEditText.setText(views.plateInclude.plateEditText.text.toString() + "-")
                        views.plateInclude.plateEditText.setSelection(views.plateInclude.plateEditText.text.length)
                    }
                }
//                if(views.plateInclude.plateEditText.text.length < 3) {
//                    views.plateInclude.plateEditText.inputType = InputType.TYPE_CLASS_NUMBER
//                }else if(views.plateInclude.plateEditText.text.length < 6) {
//                    views.plateInclude.plateEditText.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
//                }else {
//                    views.plateInclude.plateEditText.inputType = InputType.TYPE_CLASS_NUMBER
//                }
//                views.plateInclude.plateEditText.setFilters(arrayOf<InputFilter>(AllCaps()))

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {

            }

            override fun afterTextChanged(s: Editable) {

            }
        })

    }

    fun checkButton() {
        if(views.plateInclude.plateEditText.text.length == 9 && views.passwordEdittext.text.length >= 6) {
            views.loginButton.alpha = 1f
            views.loginButton.isEnabled = true
        }else {
            views.loginButton.alpha = 0.6f
            views.loginButton.isEnabled = false
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser) {
            requireActivity().window.statusBarColor = resources.getColor(R.color.background_two_color)
        }
    }

}