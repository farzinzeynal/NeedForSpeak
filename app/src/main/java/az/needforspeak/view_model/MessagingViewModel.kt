package az.needforspeak.view_model


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.needforspeak.db.AppDatabase
import az.needforspeak.model.local.ChatModel
import az.needforspeak.model.local.IncomingMessageModel
import az.needforspeak.repository.ChatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessagingViewModel(private val repository: ChatsRepository): ViewModel() {
    val onMessage = MutableLiveData<IncomingMessageModel>()
    val messages = MutableLiveData<ArrayList<ChatModel>>()
    val messageList = ArrayList<ChatModel>()


    init {
    }

    var plateNumber = ""
    fun filterList(chatlist: ArrayList<ChatModel>) {
        val list = chatlist.filter { it.plateNumber == plateNumber }.toCollection(ArrayList())
        messageList.addAll(list)
        messages.postValue(list)
    }

    fun initView(context: Context, plateNumber: String) {
        this.plateNumber = plateNumber
        val chatDao = AppDatabase.getInstance(context)?.chatDao()

        viewModelScope.launch(Dispatchers.IO) {
            val chatList = chatDao?.getChatList()?.toCollection(ArrayList())
            chatList?.let { filterList(it) }
        }

    }

    fun addChat() {

    }

    fun getChatsDb() {

    }
}