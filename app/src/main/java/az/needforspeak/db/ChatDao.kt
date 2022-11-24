package az.needforspeak.db

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query
import az.needforspeak.model.local.ChatModel

@Dao
interface ChatDao {

    @Insert
    fun insertChat(chat: ChatModel)

    @Query("SELECT * from chats")
    fun getChatList(): List<ChatModel>


}