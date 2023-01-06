package az.needforspeak.utils.image

import android.content.Context
import android.webkit.MimeTypeMap
import az.needforspeak.model.local.NFSFile
import az.needforspeak.utils.Constants
import az.needforspeak.utils.FileManager
import az.needforspeak.utils.SessionManager
import az.needforspeak.utils.network.HeaderInterceptor
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.Observable
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


object ImageDownloader {

    fun getDownloadedItems(context: Context, currentImagesList: List<String>?, mainPath: String): ArrayList<String> {
        return arrayListOf<String>().apply {
            val cachePath = FileManager.generateProfileImageFolder(context)
            currentImagesList?.forEach {
                val file = File(cachePath, "$mainPath/$it")
                if (file.exists())
                    add(it)
            }
        }
    }

    fun getLocalProfileUrl(userId: String): String {
        return "${Constants.LOCAL_PROFILE_IMAGES}${File.separator}$userId"
    }

    //--New Arch--//

    fun getSavingFile(context: Context, nfsFile: NFSFile): File {
        val storageDir = FileManager.getParentFolder(context, nfsFile)
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        return File(storageDir, "${nfsFile.fileName}${nfsFile.fileExtension}")
    }


    /**
     * This method gets files as pair and downloads it
     * first remotepath , second is local path
     */
    fun downloadMultipleFiles(context: Context, files: List<NFSFile>): Completable {
        return Observable.fromIterable(files).flatMapCompletable { entry ->
            downloadSingle(context, entry)
        }
    }

    fun downloadSingle(context: Context, nfsFile: NFSFile): Completable? {
        return Completable.create { emitter: CompletableEmitter ->
            val file = getSavingFile(context, nfsFile)
            var inputStream: InputStream? = null
            val urlString = "${Constants.DOCUMENT_URL}${nfsFile.fileName}"
            val url = URL(urlString)
            val conn = url.openConnection()
            try {
                val httpConn: HttpURLConnection = conn as HttpURLConnection
                httpConn.requestMethod = "GET"
                httpConn.setRequestProperty(HeaderInterceptor.authorization, "${HeaderInterceptor.bearer} ${SessionManager.authToken}")
                httpConn.connect()
                if (httpConn.responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpConn.inputStream
                    file.copyInputStreamToFile(inputStream)
                    emitter.onComplete()
                } else
                    emitter.onComplete()
            } catch (ex: java.lang.Exception) {
                emitter.onComplete()
            }
        }
    }

    fun uploadSingle(nfsFile: NFSFile): Completable? {
        return Completable.create { emitter: CompletableEmitter ->
            nfsFile.sendingFile?.let { file ->
                try {
                    val urlString = "${Constants.DOCUMENT_URL}${nfsFile.fileName}"
                    val client = OkHttpClient().newBuilder()
                        .build()
                    val mimeType = getMimeType(file)
                    val mediaType = mimeType?.toMediaType()
                    val body = file.asRequestBody(mediaType)
                    val request: Request = Request.Builder()
                        .url(urlString)
                        .put(body)
                        .addHeader(HeaderInterceptor.authorization, "${HeaderInterceptor.bearer} ${SessionManager.authToken}")
                        .build()
                    val response = client.newCall(request).execute()
                    if (response.isSuccessful) {
                        emitter.onComplete()
                    } else {
                        emitter.onError(RuntimeException())
                    }
                } catch (ex: Exception) {
                    emitter.onError(ex)
                }
            }
        }
    }

    fun uploadMultipleFiles(files: List<NFSFile>): Completable {
        return Observable.fromIterable(files).flatMapCompletable { entry ->
            uploadSingle(entry)
        }
    }

    private fun File.copyInputStreamToFile(inputStream: InputStream) {
        this.outputStream().use { fileOut ->
            inputStream.copyTo(fileOut)
        }
    }

    private fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

}