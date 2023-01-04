package az.needforspeak.model.local


import az.needforspeak.utils.type_defs.FileExtensionTypes
import az.needforspeak.utils.type_defs.FolderTypes
import java.io.File

class NFSFile(
    val fileName: String,
    @FolderTypes.FolderTypesDef val parentFolder: String,
    @FileExtensionTypes.FileExtensionTypesDef val fileExtension: String,
    val originalFile: File? = null,  // not needed when downloading file
    var sendingFile: File? = null // not needed when downloading file
)