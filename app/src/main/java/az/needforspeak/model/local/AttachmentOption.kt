package az.needforspeak.model.local


import az.needforspeak.R
import java.io.Serializable


class AttachmentOption(val id: Int, val title: String, val resourceImage: Int) : Serializable {

    companion object {
        const val DOCUMENT_ID: Byte = 101
        const val CAMERA_ID: Byte = 102
        const val GALLERY_ID: Byte = 103
        const val AUDIO_ID: Byte = 104
        const val LOCATION_ID: Byte = 105
        const val CONTACT_ID: Byte = 106
        val defaultList: List<AttachmentOption>
            get() {
                val attachmentOptions: MutableList<AttachmentOption> = ArrayList()
                attachmentOptions.add(
                    AttachmentOption(
                        DOCUMENT_ID.toInt(),
                        "Document",
                        R.drawable.ic_attachment_document
                    )
                )
                attachmentOptions.add(
                    AttachmentOption(
                        CAMERA_ID.toInt(),
                        "Camera",
                        R.drawable.ic_attachment_camera
                    )
                )
                attachmentOptions.add(
                    AttachmentOption(
                        GALLERY_ID.toInt(),
                        "Gallery",
                        R.drawable.ic_attachment_gallery
                    )
                )
                attachmentOptions.add(
                    AttachmentOption(
                        AUDIO_ID.toInt(),
                        "Audio",
                        R.drawable.ic_attachment_audio
                    )
                )
                attachmentOptions.add(
                    AttachmentOption(
                        LOCATION_ID.toInt(),
                        "Location",
                        R.drawable.ic_attachment_location
                    )
                )
                attachmentOptions.add(
                    AttachmentOption(
                        CONTACT_ID.toInt(),
                        "Contact",
                        R.drawable.ic_attachment_contact
                    )
                )
                return attachmentOptions
            }
    }
}