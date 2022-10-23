package az.needforspeak.ui.register

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import az.needforspeak.R
import az.needforspeak.base.BaseFragment
import az.needforspeak.component.adapter.PostAdapter
import az.needforspeak.databinding.FragmentPostBinding
import az.needforspeak.model.local.ChatModel
import az.needforspeak.model.local.CommentsModel
import az.needforspeak.model.local.PostModel
import az.needforspeak.utils.getNavOptions


class PostFragment : BaseFragment<FragmentPostBinding>(FragmentPostBinding::inflate) {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManager = LinearLayoutManager(requireContext())
        views.postRecyclerView.layoutManager = linearLayoutManager

        val uList = mutableListOf<PostModel>()
        uList.add(PostModel("Absdjd", "Kksjdkkas", "02.02.2022 22:22", "10-AA-222", "https://images.ctfassets.net/hrltx12pl8hq/3j5RylRv1ZdswxcBaMi0y7/b84fa97296bd2350db6ea194c0dce7db/Music_Icon.jpg", listOf(
            CommentsModel("", "", "", "")
        )))
        uList.add(PostModel("Lklsd", "Kksjdkkas", "02.02.2022 22:22", "10-AA-222", "https://images.ctfassets.net/hrltx12pl8hq/3j5RylRv1ZdswxcBaMi0y7/b84fa97296bd2350db6ea194c0dce7db/Music_Icon.jpg", null))

        adapter = PostAdapter(requireContext()) {
            findNavController().navigate(R.id.messagingActivity, null, getNavOptions())
        }
        adapter.addData(uList)
        views.postRecyclerView.adapter = adapter

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