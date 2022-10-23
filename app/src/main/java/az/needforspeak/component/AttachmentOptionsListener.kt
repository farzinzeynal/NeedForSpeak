package az.needforspeak.component

import az.needforspeak.model.local.AttachmentOption


interface AttachmentOptionsListener {
    fun onClick(attachmentOption: AttachmentOption?)
}