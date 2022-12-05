package az.needforspeak.model.local


import com.needforspeak.models.typedefs.FileExtensionTypes
import com.needforspeak.models.typedefs.FolderTypes
import java.io.File

class NFSFile(
    val fileName: String,
    @FolderTypes.FolderTypesDef val parentFolder: String,
    @FileExtensionTypes.FileExtensionTypesDef val fileExtension: String,
    val originalFile: File? = null,  // not needed when downloading file
    var sendingFile: File? = null // not needed when downloading file
)