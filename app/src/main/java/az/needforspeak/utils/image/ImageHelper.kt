package az.needforspeak.utils.image

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import az.needforspeak.model.local.NFSFile
import az.needforspeak.utils.FileManager
import az.needforspeak.utils.compress
import az.needforspeak.utils.getIfExists
import az.needforspeak.utils.type_defs.FileExtensionTypes
import az.needforspeak.utils.type_defs.FolderTypes
import coil.load
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import java.io.File


object ImageHelper {

    fun showProfileImage(imageView: ImageView, userId: String) {
        val folderPath = FileManager.generateCacheFolderByName(
            imageView.context,
            ImageDownloader.getLocalProfileUrl(userId)
        )
        val directory = File(folderPath)
        directory.listFiles()?.getIfExists(0)?.let {
            try {
                imageView.load(it)
            } catch (e: Exception) {
            }
        }
    }

    /**
     * @param files first remote path , second is local path
     */
    @SuppressLint("CheckResult")
    fun getFile(context: Context, fileName: String, @FileExtensionTypes.FileExtensionTypesDef fileType: String, unit: (File) -> (Unit)) {
        val nfsFile = when (fileType) {
            FileExtensionTypes.MP3 -> NFSFile(fileName, FolderTypes.VOICES, FileExtensionTypes.MP3)
            else -> NFSFile(fileName, FolderTypes.IMAGES, FileExtensionTypes.JPG)
        }
        val file = ImageDownloader.getSavingFile(context, nfsFile)
        if (file.exists()) {
            unit.invoke(file)
        } else {
            ImageDownloader.downloadSingle(context, nfsFile)
                ?.subscribeOn(io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe {
                    unit.invoke(file)
                }
        }
    }

    fun uploadFile(context: Context, nfsFile: NFSFile, result: (Boolean) -> (Unit)) {
        nfsFile.originalFile?.let {
            val savingFile = ImageDownloader.getSavingFile(context, nfsFile)
            it.copyTo(savingFile, true)
            if (nfsFile.fileExtension == FileExtensionTypes.JPG)
                savingFile.compress()
            nfsFile.sendingFile = savingFile
            try {
                ImageDownloader.uploadSingle(nfsFile)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.doOnComplete {
                        result.invoke(true)
                    }
                    ?.subscribe()
            } catch (e: Exception) {
                result.invoke(false)
            }
        }
    }

    fun downloadMultipleFiles(context: Context, files: List<NFSFile>, result: (Boolean) -> (Unit)) {
        val filteredFiles = files.filter { nfsFile -> !ImageDownloader.getSavingFile(
            context,
            nfsFile
        ).exists() } //filter all images which have not been downloaded
        ImageDownloader.downloadMultipleFiles(context, filteredFiles)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                result.invoke(false)
            }
            .doOnComplete {
                result.invoke(true)
            }
            .subscribe()
    }

    fun uploadMultipleFiles(context: Context, files: List<NFSFile>, result: (Boolean) -> (Unit)) {
        files.forEach { nfsFile ->
            val savingFile = ImageDownloader.getSavingFile(context, nfsFile)
            nfsFile.originalFile?.copyTo(savingFile, true)
            if (nfsFile.fileExtension == FileExtensionTypes.JPG)
                savingFile.compress()
            nfsFile.sendingFile = savingFile
        }
        ImageDownloader.uploadMultipleFiles(files)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                result.invoke(false)
            }
            .doOnComplete {
                result.invoke(true)
            }
            .subscribe()
    }
}
