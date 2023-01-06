package az.needforspeak.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import az.needforspeak.model.local.NFSFile
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

object FileManager {

    @SuppressLint("Range")
    fun getFileName(context: Context, uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    fun saveBitmapAsFile(bmp: Bitmap): File? {
        val outStream: OutputStream?
        val timeStamp = SimpleDateFormat("mm:ss yyyy MM dd").format(Date())
        val imageFileName = "NFS_" + timeStamp + "_.jpg"
        val extStorageDirectory = Environment.getExternalStorageDirectory().toString()
        var file = File(extStorageDirectory, imageFileName)
        if (file.exists()) {
            file.delete()
            file = File(extStorageDirectory, imageFileName)
        }
        try {
            outStream = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            outStream.flush()
            outStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return file
    }

    fun generateVoiceFolder(userId: String): String {
        return "${Environment.getExternalStorageDirectory()}${File.separator}${Constants.NFS_PARENT_PATH}${File.separator}${Constants.VOICES_PARENT_PATH}${File.separator}$userId${File.separator}"
    }

    fun generateImageFolder(userId: String): String {
        return "${Environment.getExternalStorageDirectory()}${File.separator}${Constants.NFS_PARENT_PATH}${File.separator}${Constants.IMAGES_PARENT_PATH}${File.separator}$userId${File.separator}"
    }

    fun generateProfileImageFolder(): String {
        return "${Environment.getExternalStorageDirectory()}${File.separator}${Constants.NFS_PARENT_PATH}${File.separator}${Constants.PROFILES}${File.separator}"
    }

    fun generateProfileImageFolder(context: Context): String {
        val folder = getCachedFolderDir(context)
        return "$folder${File.separator}${Constants.PROFILES}${File.separator}"
    }

    fun generatePostImageFolder(): String {
        return "${Environment.getExternalStorageDirectory()}${File.separator}${Constants.NFS_PARENT_PATH}${File.separator}${Constants.POSTS_PARENT_PATH}${File.separator}"
    }

    fun generateCacheFolderByName(context: Context, folderName: String): String {
        val folder = getCachedFolderDir(context)
        return "$folder${File.separator}$folderName${File.separator}"
    }

    fun getCachedFolderDir(context: Context): String {
        var dirPath = ""
        var imageDir: File? = null
        imageDir = File(context.cacheDir.path + "/nfs")
        if (!imageDir.exists()) {
            imageDir.mkdirs()
        }
        if (imageDir.canWrite()) {
            dirPath = imageDir.path
        }
        return dirPath
    }

    fun getParentFolder(context: Context, nfsFile: NFSFile): File {
        return File(context.filesDir, "${Constants.NFS_PARENT_PATH}${File.separator}${nfsFile.parentFolder}")
    }
}