package az.needforspeak.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import az.needforspeak.R
import az.needforspeak.base.BaseActivity
import az.needforspeak.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity: BaseActivity<ActivityHomeBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityHomeBinding = ActivityHomeBinding::inflate

    override fun onViewBindingCreated(savedInstanceState: Bundle?) {
        super.onViewBindingCreated(savedInstanceState)

        setupNavigation()


    }


    private fun setupNavigation() {
        val navView: BottomNavigationView = views.bottomNavigationView
        val navController = findNavController(R.id.navHostFragment)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{controller, destination, arguments ->

        }
    }


}