package az.needforspeak.ui.unregister


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import az.needforspeak.R

import az.needforspeak.base.BaseActivity
import az.needforspeak.databinding.ActivityUnregisterBinding
import az.needforspeak.utils.getNavOptions

class UnregisterActivity : BaseActivity<ActivityUnregisterBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityUnregisterBinding = ActivityUnregisterBinding::inflate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewBindingCreated(savedInstanceState: Bundle?) {
        super.onViewBindingCreated(savedInstanceState)


    }
}