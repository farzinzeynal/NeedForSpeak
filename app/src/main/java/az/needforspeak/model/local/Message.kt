package az.needforspeak.model.local

class Message {
    var text: String? = null
    var type: Int
    var time = 0L

    constructor(text: String?) {
        this.text = text
        type = TYPE_TEXT
    }

    constructor(time: Long) {
        this.time = time
        type = TYPE_AUDIO
    }

    companion object {
        var TYPE_TEXT = 1
        var TYPE_AUDIO = 21
    }
}