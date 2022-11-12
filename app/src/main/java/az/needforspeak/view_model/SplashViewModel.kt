package az.needforspeak.view_model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import az.needforspeak.base.BaseActivity
import az.needforspeak.repository.AuthRepositry
import az.needforspeak.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashViewModel(private val authRepositry: AuthRepositry): ViewModel() {
    val showError = MutableLiveData<Boolean>()
    val successConnect = MutableLiveData<Boolean>()

    fun login(context: Context, userName: String, password: String) = run {
        BaseActivity.loadingUp()
        GlobalScope.launch(Dispatchers.IO) {
            try { val result = authRepositry.login(userName, password)
                if (result == true) {
                    SessionManager.saveLogin(context, userName, password)
                    successConnect.postValue(true)
                } else {
                    showError.postValue(true)
                }
                BaseActivity.loadingDown()
            } catch (e: Exception) {
                showError.postValue(true)
                BaseActivity.loadingDown()
            }
        }
    }
}