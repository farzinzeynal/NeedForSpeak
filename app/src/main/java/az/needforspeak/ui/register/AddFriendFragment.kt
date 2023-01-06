package az.needforspeak.ui.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import az.needforspeak.R
import az.needforspeak.base.BaseActivity
import az.needforspeak.base.BaseFragment
import az.needforspeak.component.adapter.MarketAdapter
import az.needforspeak.databinding.FragmentAddFriendBinding
import az.needforspeak.utils.*
import az.needforspeak.view_model.FriendsViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_create_post.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddFriendFragment :
    BaseFragment<FragmentAddFriendBinding>(FragmentAddFriendBinding::inflate) {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: MarketAdapter
    private val viewModel: FriendsViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        views.plateInclude.plateEditText.addTextChangedListener(
            MaskFormatter(
                "99_AA_999",
                views.plateInclude.plateEditText
            )
        )
        views.plateInclude.plateEditText.doOnTextChanged { text, start, before, count ->
            text?.let {
                if (it.length >= 9) {
                    views.addFriendBtn.alpha = 1f
                    views.addFriendBtn.isEnabled = true
                } else {
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

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })




        views.addFriendBtn.setOnClickListener {
            viewModel.searchUser(views.plateInclude.plateEditText.text.toString())
        }

        views.btnRetry.setOnClickListener {
            views.findUserLayout.visibility = View.VISIBLE
            views.sendRequestLayout.visibility = View.GONE
            views.userFullName.visibility = View.GONE
            views.plateInclude.plateEditText.text.clear()
        }

        views.btnSendRequest.setOnClickListener {
            viewModel.sendFriendRequest(views.plateLayout.plateNum.text.toString())
            Toast.makeText(requireContext(), "Friend request sent", Toast.LENGTH_LONG).show()
            views.findUserLayout.visibility = View.VISIBLE
            views.userFullName.visibility = View.GONE
            views.sendRequestLayout.visibility = View.GONE
            views.plateInclude.plateEditText.text.clear()
        }


        viewModel.userSearchLiveData.observe(viewLifecycleOwner) { user ->
            BaseActivity.loadingDown()
            if (user.isNotNull() && user.data?.name?.value!=null && user.data.surname?.value!=null) {
                views.userFullName.text = user.data.name.value + " " + user.data.surname.value
                views.userFullName.visibility = View.VISIBLE
                views.findUserLayout.visibility = View.GONE
                views.sendRequestLayout.visibility = View.VISIBLE
                views.plateLayout.plateNum.text = views.plateInclude.plateEditText.text.toString()
                downloadUserImage(views.plateInclude.plateEditText.text.toString())
                hideKeyboard(requireActivity())
            } else {
                Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun downloadUserImage(userId: String) {
        val url = "https://api.needforspeak.xyz/profile/$userId/profile-picture"
        val auth: LazyHeaders = LazyHeaders.Builder() // can be cached in a field and reused
            .addHeader("Authorization", AuthUtils.token)
            .build()

        Glide.with(this).load(GlideUrl(url, auth)).error(R.drawable.ic_profile).into(views.userProfileImage)
    }

    private fun getUserInfo(userString: String?): UserInfo? {
        return try {
            Gson().fromJson(userString, UserInfo::class.java)
        } catch (e: Exception) {
            UserInfo("")
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