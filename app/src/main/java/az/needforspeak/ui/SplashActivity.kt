package az.needforspeak.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.webkit.WebView
import az.needforspeak.base.BaseActivity
import az.needforspeak.databinding.ActivitySplashBinding
import az.needforspeak.ui.unregister.LoginViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject


class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivitySplashBinding = ActivitySplashBinding::inflate
    val sharedPreferences by inject<SharedPreferences> { parametersOf("secure") }
    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewBindingCreated(savedInstanceState: Bundle?) {
        super.onViewBindingCreated(savedInstanceState)
        val ua = WebView(this).settings.userAgentString
        sharedPreferences.edit().putString("user_agent", ua).apply()
//        startActivity(Intent(this, MainActivity::class.java))
//        finish()

        viewModel.successConnect.observe(this) {
            if(it) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        viewModel.showError.observe(this) {
            sharedPreferences.edit().remove("isLogin").apply()
            sharedPreferences.edit().remove("username").apply()
            sharedPreferences.edit().remove("password").apply()
            startActivity(Intent(this, UnregisterActivity::class.java))
            finish()
        }

        if(sharedPreferences.getBoolean("isLogin", false)) {
            val username =  sharedPreferences.getString("username", "") ?: run { "" }
            val password = sharedPreferences.getString("password", "") ?: run { "" }
            viewModel.login(this, username, password)
        }else {
            startActivity(Intent(this, UnregisterActivity::class.java))
            finish()
        }

    }
}