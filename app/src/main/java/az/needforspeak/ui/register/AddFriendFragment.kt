package az.needforspeak.ui.register

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import az.needforspeak.R
import az.needforspeak.base.BaseActivity
import az.needforspeak.base.BaseFragment
import az.needforspeak.component.adapter.MarketAdapter
import az.needforspeak.databinding.FragmentAddFriendBinding
import az.needforspeak.utils.Extentions.showToast
import az.needforspeak.utils.MaskFormatter
import az.needforspeak.utils.hideKeyboard
import az.needforspeak.view_model.FriendsViewModel
import kotlinx.android.synthetic.main.activity_create_post.*
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

            BaseActivity.loadingUp()
            Handler().postDelayed({
                BaseActivity.loadingDown()
                views.findUserLayout.visibility = View.GONE
                views.sendRequestLayout.visibility = View.VISIBLE
                views.plateLayout.plateNum.text = views.plateInclude.plateEditText.text.toString()
            },3000)
        }

        views.btnRetry.setOnClickListener {
            views.findUserLayout.visibility = View.VISIBLE
            views.sendRequestLayout.visibility = View.GONE
            views.plateInclude.plateEditText.text.clear()
        }

        views.btnSendRequest.setOnClickListener {
            Toast.makeText(requireContext(), "Friend request sent",Toast.LENGTH_LONG).show()
            views.findUserLayout.visibility = View.VISIBLE
            views.sendRequestLayout.visibility = View.GONE
            views.plateInclude.plateEditText.text.clear()
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