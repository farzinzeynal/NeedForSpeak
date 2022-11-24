package az.needforspeak.ui.register

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import az.needforspeak.R
import az.needforspeak.base.BaseFragment
import az.needforspeak.component.adapter.UserAdapter
import az.needforspeak.databinding.FragmentFriendsBinding
import az.needforspeak.model.local.UserModel
import az.needforspeak.utils.getNavOptions


class FriendsFragment : BaseFragment<FragmentFriendsBinding>(FragmentFriendsBinding::inflate) {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutManager = LinearLayoutManager(requireContext())
        views.friendsRecyclerView.layoutManager = linearLayoutManager
        views.friendsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                views.friendsRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL
            ))

        val uList = mutableListOf<UserModel>()
        uList.add(UserModel(plateNumber =  "10-BB-223", name = "Test", surname = "Tester", phone = "+9947092992", photo = "https://thumbs.dreamstime.com/b/portrait-young-beautiful-girl-fashion-photo-29870052.jpg", desc = "Llsd", status = "", career = "", education = null, interests = null))
        uList.add(UserModel(plateNumber =  "10-AA-999", name = "asdad", surname = "asdad", phone = "+9947092992", photo = "https://thumbs.dreamstime.com/b/portrait-young-beautiful-girl-fashion-photo-29870052.jpg", desc = "Llsd", status = "", career = "", education = null, interests = null))
        uList.add(UserModel(plateNumber =  "10-BB-555", name = "sad", surname = "asd", phone = "+9947092992", photo = "https://thumbs.dreamstime.com/b/portrait-young-beautiful-girl-fashion-photo-29870052.jpg", desc = "Llsd", status = "", career = "", education = null, interests = null))
        uList.add(UserModel(plateNumber =  "10-CC-444", name = "Test", surname = "Tester", phone = "+9947092992", photo = "https://thumbs.dreamstime.com/b/portrait-young-beautiful-girl-fashion-photo-29870052.jpg", desc = "Llsd", status = "", career = "", education = null, interests = null))
        adapter = UserAdapter(requireContext()) {
            findNavController().navigate(R.id.accountFragment, null, getNavOptions())
        }
        adapter.addData(uList)
        views.friendsRecyclerView.adapter =adapter

        views.searchInput.setSearch {
            adapter.filter.filter(it)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser) {
            requireActivity().window.statusBarColor = resources.getColor(R.color.background_two_color)
        }
    }
}