package az.needforspeak.ui.unregister


import android.os.Bundle
import android.view.LayoutInflater

import az.needforspeak.base.BaseActivity
import az.needforspeak.databinding.ActivityUnregisterBinding

class UnregisterActivity : BaseActivity<ActivityUnregisterBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityUnregisterBinding = ActivityUnregisterBinding::inflate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewBindingCreated(savedInstanceState: Bundle?) {
        super.onViewBindingCreated(savedInstanceState)
    }
}