package com.needforspeak.models.typedefs

import androidx.annotation.StringDef

object FolderTypes {
    const val IMAGES = "images"
    const val VOICES = "voices"

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(IMAGES, VOICES)
    annotation class FolderTypesDef
}
