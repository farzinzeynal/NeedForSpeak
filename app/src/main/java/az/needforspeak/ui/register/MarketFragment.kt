package az.needforspeak.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import az.needforspeak.R
import az.needforspeak.base.BaseFragment
import az.needforspeak.component.adapter.MarketAdapter
import az.needforspeak.component.adapter.UserAdapter
import az.needforspeak.databinding.FragmentMarketBinding
import az.needforspeak.model.local.MarketModel
import az.needforspeak.ui.MainActivity
import az.needforspeak.utils.getNavOptions


class MarketFragment : BaseFragment<FragmentMarketBinding>(FragmentMarketBinding::inflate) {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: MarketAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = requireArguments().getString("type", "")

        linearLayoutManager = LinearLayoutManager(requireContext())
        views.marketRecyclerView.layoutManager = linearLayoutManager
        views.marketRecyclerView.addItemDecoration(
            DividerItemDecoration(
                views.marketRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        val mList = mutableListOf<MarketModel>()
        if (type == "in") {
            mList.add(MarketModel("https://banner2.cleanpng.com/20180718/oyl/kisspng-2018-bmw-3-series-car-bmw-x3-bmw-i-bmw-m1-logo-5b4ff21bcee538.0268729015319659798475.jpg", "BMW"))
        } else {
            mList.add(MarketModel(R.drawable.ic_car_key, "Cars"))
        }
        adapter = MarketAdapter(requireContext()) {
            if (type.isEmpty()) {
                val navC = Navigation.findNavController(requireView())
                navC.graph.findNode(R.id.marketFragmentIn)?.label = it.title
                navC.navigate(R.id.marketFragmentIn, bundleOf("title" to "BMW", "type" to "in"), getNavOptions())
            }else {
                val navC = Navigation.findNavController(requireView())
                navC.graph.findNode(R.id.postFragment)?.label = it.title
                navC.navigate(R.id.postFragment, null, getNavOptions())
            }
        }
        adapter.addData(mList)

        views.marketRecyclerView.adapter = adapter

        views.searchInput.setSearch {
            adapter.filter.filter(it)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            requireActivity().window.statusBarColor =
                resources.getColor(R.color.background_two_color)
        }
    }
}