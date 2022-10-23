package az.needforspeak.ui.register

import android.content.Context
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.needforspeak.db.AppDatabase
import az.needforspeak.db.ChatDao
import az.needforspeak.model.local.ChatModel
import az.needforspeak.model.local.IncomingMessageModel
import az.needforspeak.repository.ChatsRepository
import az.needforspeak.utils.XMPPController
import az.needforspeak.utils.getCurrentDateTime
import az.needforspeak.utils.isNotNull
import az.needforspeak.utils.toString
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.packet.Message
import org.json.JSONArray
import org.json.JSONObject
import org.jxmpp.jid.EntityBareJid
import org.koin.androidx.compose.inject

class ChatsViewModel(private val repository: ChatsRepository): ViewModel() {
    val onMessage = MutableLiveData<IncomingMessageModel>()
    val chats = MutableLiveData<ArrayList<ChatModel>>()



    init {
    }


    fun filterList(chatlist: ArrayList<ChatModel>) {
        val newList = arrayListOf<ChatModel>()
        chatlist?.forEach { chat ->
            val newEl = newList.find { it.plateNumber == chat.plateNumber }
            newEl?.let {
                if(it.id > chat.id) {
                    newList.set(newList.indexOf(newEl), chat)
                }
            }?: run {
                newList.add(chat)
            }
        }
        chats.postValue(newList)
    }

    fun initView(context: Context) {
        val chatDao = AppDatabase.getInstance(context)?.chatDao()

        viewModelScope.launch(Dispatchers.IO) {
            val chatList = chatDao?.getChatList()?.toCollection(ArrayList())
            chatList?.let { filterList(it) }

        }
        XMPPController.addIncomingMessagesListener(object : IncomingChatMessageListener {
            override fun newIncomingMessage(from: EntityBareJid?, message: Message?, chat: Chat?) {
                val fromss =  Gson().toJson(from)
                val messagess = Gson().toJson(message)
                val chatList =  chats.value
                chatList?.removeAll{ it.plateNumber ==  from?.localpart.toString() }

                chatList?.add(ChatModel(0, plateNumber =  from?.localpart.toString(), name = "Test", surname = "Tester", phone = "+9947092992", photo = "https://thumbs.dreamstime.com/b/portrait-young-beautiful-girl-fashion-photo-29870052.jpg", status = "", career = "", education = null, interests = null, message = message?.body, getCurrentDateTime().toString("HH:mm")))

                chatList?.let {
                    chats.postValue(it)
                }

                chatDao?.insertChat(ChatModel(0, plateNumber =  from?.localpart.toString(), name = "Test", surname = "Tester", phone = "+9947092992", photo = "https://thumbs.dreamstime.com/b/portrait-young-beautiful-girl-fashion-photo-29870052.jpg", status = "", career = "", education = null, interests = null, message = message?.body, getCurrentDateTime().toString("HH:mm")))

                onMessage.postValue(IncomingMessageModel(from, message, chat))
            }

        })
    }

    fun addChat() {

    }

    fun getChatsDb() {

    }
}