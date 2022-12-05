package az.needforspeak.utils

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

object Extentions {
    fun AppCompatActivity.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}