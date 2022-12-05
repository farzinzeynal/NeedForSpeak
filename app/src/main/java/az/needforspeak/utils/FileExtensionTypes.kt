package com.needforspeak.models.typedefs

import androidx.annotation.StringDef

object FileExtensionTypes {
    const val JPG = ".jpg"
    const val MP3 = ".mp3"

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(JPG, MP3)
    annotation class FileExtensionTypesDef
}
