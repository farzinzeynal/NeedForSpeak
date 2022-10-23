package az.needforspeak.model.local

import android.os.CountDownTimer
import android.os.Handler


data class MessageModel (
    var message: String? = null,
    var date: String? = null,
    var type: MESSAGE_TYPE,
    var img: String? = null,
    var voice: String? = null,
    var sender: MESSAGE_SENDER,
    var playState: PLAY_STATE = PLAY_STATE.PAUSE,
)

enum class PLAY_STATE {
    PLAY,
    PAUSE,
    DOWNLOAD
}

enum class MESSAGE_TYPE {
    TEXT,
    VOICE,
    PHOTO
}

enum class MESSAGE_SENDER {
    TO,
    FROM
}