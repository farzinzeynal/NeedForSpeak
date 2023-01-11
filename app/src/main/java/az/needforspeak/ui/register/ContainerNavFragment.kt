package az.needforspeak.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import az.needforspeak.R
import az.needforspeak.base.BaseFragment
import az.needforspeak.databinding.FragmentContainerNavBinding


class ContainerNavFragment : BaseFragment<FragmentContainerNavBinding>(FragmentContainerNavBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()

        /*       val navController = views.containerContent.findNavController()
        val navGraph = navController.navInflater.inflate(R.navigation.main_nav)
        val navId =requireArguments().getInt("navId")
        if(navId != null) {
            navGraph.startDestination = navId
        }else {
            navGraph.startDestination = R.id.friendsFragment
        }
        views.containerContent.findNavController().graph = navGraph
    }

    companion object {
        fun newInstance(nav: Int): ContainerNavFragment {
            val args = Bundle()
            args.putInt("navId", nav)
            val fragment = ContainerNavFragment()
            fragment.arguments = args
            return fragment

        }
    }*/
    }
}