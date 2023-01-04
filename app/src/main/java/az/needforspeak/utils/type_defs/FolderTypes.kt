package az.needforspeak.utils.type_defs

import androidx.annotation.StringDef

object FolderTypes {
    const val IMAGES = "images"
    const val VOICES = "voices"

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(IMAGES, VOICES)
    annotation class FolderTypesDef
}
