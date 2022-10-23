package az.needforspeak.component.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import az.needforspeak.R
import az.needforspeak.model.local.Message
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val messages: MutableList<Message> = ArrayList()
    fun add(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.lastIndexOf(message))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_message, null)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MessageViewHolder).bind(messages[position])
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    inner class MessageViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        var text: TextView
        fun bind(message: Message) {
            if (message.type === Message.TYPE_AUDIO) {
                text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_microphone, 0, 0, 0)
                text.text = getAudioTime(message.time)
            } else if (message.type === Message.TYPE_TEXT) {
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                text.setText(message.text)
            } else {
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                text.text = ""
            }
        }

        init {
            text = itemView.findViewById(R.id.textView)
        }
    }

    companion object {
        private lateinit var timeFormatter: SimpleDateFormat
        fun getAudioTime(time: Long): String {
            var time = time
            time *= 1000
            timeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"))
            return timeFormatter.format(Date(time))
        }
    }

    init {
        timeFormatter = SimpleDateFormat("m:ss", Locale.getDefault())
    }
}