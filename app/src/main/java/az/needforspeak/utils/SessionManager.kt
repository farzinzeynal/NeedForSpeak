package az.needforspeak.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.jxmpp.jid.Jid
import org.jxmpp.jid.impl.JidCreate
import java.util.*


object SessionManager {
    private var mApplicationContext: Context? = null
    private var userId: String? = null
    const val USERNAME = "username"
    const val PASSWORD = "password"
    const val authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJNYW1tYWRiYXlsaSIsImlhdCI6MTYwNDI0NjUxNSwiZXhwIjoxOTUxNTc0NTE1LCJhdWQiOiJhcGkubWFtbWFkYmF5bGkuY29tIiwic3ViIjoiamF2YWRAbWFtbWFkYmF5bGkuY29tIn0.fb1Vy5DzmaExiRoNAEWlB4F46KyLv2oO9acDNpTF7Us"

    fun initSession(userName: String) {
        userId = userName
    }

    fun initSession(applicationContext: Context) {
        this.mApplicationContext = applicationContext
    }

    fun getCurrentUserId(): String {
        return userId ?: ""
    }

    fun getCurrentUser(): String {
        return "${userId}@${Constants.XMPP_HOST}"
    }

    fun getCurrentUserJID(): Jid {
        return JidCreate.bareFrom(getCurrentUser())
    }

    fun saveLogin(mContext: Context, userName: String, password: String) {
        getSharedPrefs(mContext).edit().apply {
            putString(USERNAME, userName)
            putString(PASSWORD, password)
        }.apply()
    }

    fun getRegisteredUser(mContext: Context? = mApplicationContext): Pair<String?, String?>? {
        mContext?.let {
            getSharedPrefs(mContext).apply {
                return Pair(getString(USERNAME, null), getString(PASSWORD, null))
            }
        }
        return null
    }

    fun isUserRegistered(mContext: Context): Boolean {
        getSharedPrefs(mContext).apply {
            return contains(USERNAME)
        }
    }

    fun clearUserData(mContext: Context) {
        getSharedPrefs(mContext).apply {
            edit().apply {
                remove(USERNAME)
                remove(PASSWORD)
            }.apply()
        }
    }


    fun getRegisteredUserId(mContext: Context): String? {
        getSharedPrefs(mContext).apply {
            return getString(USERNAME, null)
        }
    }

    fun saveFirebaseToken(mContext: Context, token: String) {
        getSharedPrefs(mContext).edit().apply {
            putString(SharedConstants.NOTIFICATION_TOKEN, token)
            putBoolean(SharedConstants.PENDING_TOKEN, true)
        }.apply()
    }

    fun havePendingFirebaseToken(context: Context): Boolean {
        return getSharedPrefs(context).getBoolean(SharedConstants.PENDING_TOKEN, false)
    }

    fun getSharedPrefs(mContext: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(mContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            mContext,
            SharedTypes.USERDATA,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

}