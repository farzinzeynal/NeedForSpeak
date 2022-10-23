package az.needforspeak.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
class ChatModel (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var plateNumber: String,
    var name: String,
    var surname: String,
    var phone: String?,
    var photo: String?,
    var status: String?,
    var career: String?,
    var education: String?,
    var interests: String?,
    var message: String?,
    var date: String?
)