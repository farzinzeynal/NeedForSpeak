package az.needforspeak.model.remote.auth

import androidx.room.*
import az.needforspeak.utils.FriendListType
import az.needforspeak.utils.UserType
import az.needforspeak.utils.fromJidToString
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.jxmpp.jid.Jid


@Entity(tableName = "user", indices = [Index(value = ["userID"], unique = true)])
class User(

    @PrimaryKey
    @ColumnInfo(name = "userID")
    var userId: String,

    @ColumnInfo(name = "userJID")
    var userJID: String? = null,

    @ColumnInfo(name = "lastName")
    var lastName: String? = null,

    @ColumnInfo(name = "firstName")
    var firstName: String? = null,

    @ColumnInfo(name = "day_of_birth")
    var dayOfBirth: String? = null,

    @ColumnInfo(name = "phone_number")
    var phoneNumber: String? = null,

    @ColumnInfo(name = "additional")
    var additional: AdditionalInfo? = null,

    @ColumnInfo(name = "user_type")
    @UserType.UserTypeDef var userType: Int? = UserType.TYPE_NORMAL
) {
    @Ignore
    var type: Int = FriendListType.TYPE_FRIEND

    override fun equals(other: Any?): Boolean {
        if (other is User)
            return userJID.equals(other.userJID, true)
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = userId?.hashCode() ?: 0
        result = 31 * result + (lastName?.hashCode() ?: 0)
        result = 31 * result + (firstName?.hashCode() ?: 0)
        return result
    }

    companion object {
        fun fromJid(jid: Jid?): User {
            return User(jid.fromJidToString() ?: "", jid?.asUnescapedString())
        }
    }

    fun contains(searchString: String): Boolean {
        return userId.contains(searchString, true)
                || firstName?.contains(searchString, true) == true
                || lastName?.contains(searchString, true) == true
    }

}


data class AdditionalInfo(
    @SerializedName("user_username")
    var username: String? = null,

    @SerializedName("user_career")
    var career: String? = null,

    @SerializedName("user_about")
    var about: String? = null,

    @SerializedName("user_education")
    var education: String? = null,

    @SerializedName("user_interests")
    var interests: String? = null,

    @SerializedName("user_day_of_birth")
    var dayOfBirth: String? = null,

    @SerializedName("user_martial_status")
    var martialStatus: String? = null,

    @SerializedName("user_photos")
    var userPhotos: String? = null

) {
    fun getUserProfileImages(): ArrayList<String?> {
        return try {
            Gson().fromJson<ArrayList<String?>>(userPhotos, ArrayList::class.java)
        } catch (e: Exception) {
            arrayListOf()
        }
    }
}
