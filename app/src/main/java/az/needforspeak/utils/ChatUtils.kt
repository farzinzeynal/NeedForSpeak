package az.needforspeak.utils

import android.content.Context
import android.widget.ImageView
import az.needforspeak.utils.image.ImageHelper
import az.needforspeak.utils.type_defs.FileExtensionTypes
import coil.load
import com.bumptech.glide.Glide


object ChatUtils {
    fun loadImageFromStorageIntoImageView(imageView: ImageView, source: String?, context: Context) {
        source?.let {
            ImageHelper.getFile(imageView.context, source, FileExtensionTypes.JPG) { file ->
                try {
                    Glide.with(context).load(file.toString()).into(imageView)
                }
                catch (ex: Exception){
                    ex
                }
            }
        }
    }

}