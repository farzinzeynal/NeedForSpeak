package az.needforspeak.ui.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import az.needforspeak.R
import az.needforspeak.base.BaseFragment
import az.needforspeak.component.adapter.MarketAdapter
import az.needforspeak.databinding.FragmentAddFriendBinding
import az.needforspeak.utils.MaskFormatter
import az.needforspeak.utils.hideKeyboard
import az.needforspeak.view_model.FriendsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddFriendFragment : BaseFragment<FragmentAddFriendBinding>(FragmentAddFriendBinding::inflate) {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: MarketAdapter
    private val viewModel: FriendsViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        views.plateInclude.plateEditText.addTextChangedListener(MaskFormatter("99_AA_999", views.plateInclude.plateEditText))
        views.plateInclude.plateEditText.doOnTextChanged { text, start, before, count ->
            text?.let {
                if(it.length >= 9) {
                    views.addFriendBtn.alpha = 1f
                    views.addFriendBtn.isEnabled = true
                }else {
                    views.addFriendBtn.alpha = 0.6f
                    views.addFriendBtn.isEnabled = false
                }
            }
        }

        views.plateInclude.plateEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (views.plateInclude.plateEditText.text.length + 1 === 3 || views.plateInclude.plateEditText.text.length + 1 === 6) {
                    if (before - count < 0) {
                        views.plateInclude.plateEditText.setText(views.plateInclude.plateEditText.text.toString() + "-")
                        views.plateInclude.plateEditText.setSelection(views.plateInclude.plateEditText.text.length)
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable) { }
        })


        views.addFriendBtn.setOnClickListener {
            val userDatas = viewModel.searchUser(views.plateInclude.plateEditText.text.toString())
            userDatas
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard(requireActivity())
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            requireActivity().window.statusBarColor =
                resources.getColor(R.color.background_two_color)
        }
    }
}