package az.needforspeak.component.adapter

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import az.needforspeak.R
import az.needforspeak.model.local.MESSAGE_SENDER
import az.needforspeak.model.local.MESSAGE_TYPE
import az.needforspeak.model.local.MessageModel
import az.needforspeak.model.local.PLAY_STATE
import az.needforspeak.utils.getDpToPixel
import coil.load
import coil.size.Scale
import java.io.IOException
import java.util.concurrent.TimeUnit


class MessagesAdapter(context: Context, private val dataSet: MutableList<MessageModel>) :
    RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    val context = context
    private var runnable: Runnable? = null
    private var handler: Handler = Handler()
    var displayMetrics = context.resources.displayMetrics
    var dpHeight = displayMetrics.heightPixels / displayMetrics.density
    var dpWidth = displayMetrics.widthPixels / displayMetrics.density
    val players = HashMap<Int, MediaPlayer?>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text_message: TextView
        val date_message: TextView
        val message_layout: LinearLayout
        val message_main_layout: LinearLayout
        val photo_message: ImageView
        val voice_message: LinearLayout
        val duretion_message: TextView
        val play_message: ImageView
        val slider_message: SeekBar
        init {
            // Define click listener for the ViewHolder's View.
            date_message = view.findViewById(R.id.date_message)
            text_message = view.findViewById(R.id.text_message)
            message_layout = view.findViewById(R.id.message_layout)
            message_main_layout = view.findViewById(R.id.message_main_layout)
            photo_message = view.findViewById(R.id.photo_message)
            voice_message = view.findViewById(R.id.voice_message)
            duretion_message = view.findViewById(R.id.duretion_message)
            play_message = view.findViewById(R.id.play_message)
            slider_message = view.findViewById(R.id.slider_message)
        }

    }

    fun onDestroy() {
        players.forEach {
            it.value?.pause()
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.messages_view, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if(dataSet[position].sender == MESSAGE_SENDER.FROM) {
            viewHolder.message_layout.setBackgroundResource(R.drawable.message_round_sender)
            viewHolder.message_main_layout.gravity = Gravity.END
            viewHolder.message_main_layout.setPadding(
                getDpToPixel(context, 70f),
                0,
                getDpToPixel(context, 10f),
                0
            )
        }else {
            viewHolder.message_layout.setBackgroundResource(R.drawable.message_round)
            viewHolder.message_main_layout.gravity = Gravity.START
            viewHolder.message_main_layout.setPadding(
                getDpToPixel(context, 10f),
                0,
                getDpToPixel(context, 70f),
                0
            )
        }

        when(dataSet[position].type) {
            MESSAGE_TYPE.TEXT -> {
                viewHolder.text_message.text = dataSet[position].message
                viewHolder.text_message.visibility = View.VISIBLE
                viewHolder.photo_message.visibility = View.GONE
                viewHolder.voice_message.visibility = View.GONE
                viewHolder.duretion_message.visibility = View.GONE
            }
            MESSAGE_TYPE.PHOTO -> {
//                viewHolder.photo_message.load(dataSet[position].img)
                viewHolder.photo_message.load(dataSet[position].img) {
                    crossfade(750)
                    scale(Scale.FILL)
                }

                viewHolder.photo_message.visibility = View.VISIBLE
                viewHolder.text_message.visibility = View.GONE
                viewHolder.voice_message.visibility = View.GONE
                viewHolder.duretion_message.visibility = View.GONE
            }
            MESSAGE_TYPE.VOICE -> {
                viewHolder.voice_message.layoutParams.width = getDpToPixel(context, dpWidth - 105f)
                players[position] = MediaPlayer().apply {
                    try {
                        setDataSource(context, Uri.parse(dataSet[position].voice))
                        prepare()
                    } catch (e: IOException) {
                    }
                }
                players[position]?.let { player ->

                    viewHolder.duretion_message.text = calculateDuration(player.duration)
                    viewHolder.slider_message.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fr: Boolean) {
                            if(fr) {
                                player?.seekTo(progress)
                                viewHolder.duretion_message.text = calculateDuration(progress)
                            }
                        }
                        override fun onStartTrackingTouch(p0: SeekBar?) {}
                        override fun onStopTrackingTouch(p0: SeekBar?) {}
                    })
                    viewHolder.slider_message.max = player.duration
                    viewHolder.play_message.setOnClickListener {

                        if(dataSet[position].playState == PLAY_STATE.PAUSE) {
//                            players.forEach {
//                                it.value?.pause()
//                                dataSet[it.key].playState = PLAY_STATE.PAUSE
//                                viewHolder.play_message.setImageResource(R.drawable.ic_play)
//                            }
                            dataSet[position].playState = PLAY_STATE.PLAY
                            viewHolder.play_message.setImageResource(R.drawable.ic_pause)
                            player.start()

                            runnable = object: Runnable {
                                override fun run() {
                                    runnable?.let { it1 -> handler.postDelayed(it1, 100) }
                                    viewHolder.slider_message.progress = player.currentPosition
                                    viewHolder.duretion_message.text = calculateDuration(player.currentPosition)
                                }
                            }
                            runnable?.run()

                            player.setOnCompletionListener {
                                viewHolder.slider_message.progress = 1
                                dataSet[position].playState = PLAY_STATE.PAUSE
                                viewHolder.play_message.setImageResource(R.drawable.ic_play)
                            }

                        }else if(dataSet[position].playState == PLAY_STATE.PLAY) {
                            dataSet[position].playState = PLAY_STATE.PAUSE
                            viewHolder.play_message.setImageResource(R.drawable.ic_play)
                            player.pause()
                        }
                    }
                }

                viewHolder.voice_message.visibility = View.VISIBLE
                viewHolder.duretion_message.visibility = View.VISIBLE
                viewHolder.text_message.visibility = View.GONE
                viewHolder.photo_message.visibility = View.GONE
            }
        }

        viewHolder.date_message.text = dataSet[position].date
    }

    private fun calculateDuration(duration: Int): String? {
        var finalDuration = ""
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration.toLong())
        if (minutes == 0L) {
            finalDuration = if(seconds > 9) "0:$seconds" else "0:0$seconds"
        } else {
            if (seconds >= 60) {
                val sec = seconds - minutes * 60
                val secstr = if(seconds > 9) sec.toString() else "0:$sec"
                finalDuration = "$minutes:$secstr"
            }
        }
        return finalDuration
    }

    override fun getItemCount() = dataSet.size

}