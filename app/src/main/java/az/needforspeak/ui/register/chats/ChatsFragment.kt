package az.needforspeak.ui.register.chats

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import az.needforspeak.R
import az.needforspeak.base.BaseFragment
import az.needforspeak.component.adapter.ChatAdapter
import az.needforspeak.databinding.FragmentChatsBinding
import az.needforspeak.model.local.ChatModel
import az.needforspeak.view_model.ChatsViewModel
import az.needforspeak.utils.getNavOptions
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChatsFragment : BaseFragment<FragmentChatsBinding>(FragmentChatsBinding::inflate) {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: ChatAdapter
    private val viewModel: ChatsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initView(requireContext())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutManager = LinearLayoutManager(requireContext())
        views.chatRecyclerView.layoutManager = linearLayoutManager
        views.chatRecyclerView.addItemDecoration(
            DividerItemDecoration(
                views.chatRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        val uList = mutableListOf<ChatModel>()
        uList.add(ChatModel(id= 1, plateNumber =  "10-BB-223", name = "Test", surname = "Tester", phone = "+9947092992", photo = "https://thumbs.dreamstime.com/b/portrait-young-beautiful-girl-fashion-photo-29870052.jpg", status = "", career = "", education = null, interests = null, message = "Bla blaksd je", date = "03:99"))
        uList.add(ChatModel(id= 2, plateNumber =  "10-AA-999", name = "asdad", surname = "asdad", phone = "+9947092992", photo = "https://thumbs.dreamstime.com/b/portrait-young-beautiful-girl-fashion-photo-29870052.jpg", status = "", career = "", education = null, interests = null, message = "Ljsd msmd",  date = "09:00"))
        uList.add(ChatModel(id= 3, plateNumber =  "10-BB-555", name = "sad", surname = "asd", phone = "+9947092992", photo = "https://thumbs.dreamstime.com/b/portrait-young-beautiful-girl-fashion-photo-29870052.jpg", status = "", career = "", education = null, interests = null, message = "testlsld", date = "11:40"))

        adapter = ChatAdapter(requireContext()) {
            findNavController().navigate(R.id.messagingActivity, bundleOf("plateNumber" to it.plateNumber), getNavOptions())
        }

        adapter.addData(uList)



        views.chatRecyclerView.adapter = adapter
        views.searchInput.setSearch {
            adapter.filter.filter(it)
        }

       /* viewModel.onMessage.observe(viewLifecycleOwner) {
            if(it != null) {
                val uList = mutableListOf<ChatModel>()
                uList.add(ChatModel(0, plateNumber =  it.from?.localpart.toString(), name = "Test", surname = "Tester", phone = "+9947092992", photo = "https://thumbs.dreamstime.com/b/portrait-young-beautiful-girl-fashion-photo-29870052.jpg", status = "", career = "", education = null, interests = null, message = it.message?.body, getCurrentDateTime().toString("HH:mm")))

            }
        }

        viewModel.chats.observe(viewLifecycleOwner) {
            adapter.addData(it)
            adapter.notifyDataSetChanged()
            viewModel.onMessage.postValue(null)
        }

        linearLayoutManager = LinearLayoutManager(requireContext())
        views.chatRecyclerView.layoutManager = linearLayoutManager
        views.chatRecyclerView.addItemDecoration(
            DividerItemDecoration(
                views.chatRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )
*/


    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser) {
            requireActivity().window.statusBarColor = resources.getColor(R.color.background_color)
        }
    }
}