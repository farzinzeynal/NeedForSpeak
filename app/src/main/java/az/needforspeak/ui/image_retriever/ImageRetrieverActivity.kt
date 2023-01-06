package az.needforspeak.ui.image_retriever

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import az.needforspeak.base.BaseActivity
import az.needforspeak.component.adapter.ImagesAdapter
import az.needforspeak.databinding.ActivityImageRetrieverBinding
import az.needforspeak.model.local.Image
import az.needforspeak.utils.*
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream
import java.io.File

class ImageRetrieverActivity : BaseActivity<ActivityImageRetrieverBinding>(),
    ImagesAdapter.OnImageItemClick<Image> {

    private val pickImageFromMemoryPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private val pickImageFromCameraPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    private val capturedImages: ArrayList<Image> = arrayListOf()
    private var imagesAdapter: ImagesAdapter? = null
    private var mLastSelected: Image? = null

    private val pickImagePermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            if (result?.all { it.value == true } == true) {
                startRetrieveAction()
            }
        }

    private val takePictureResultFromCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val photo = result?.data?.extras?.get("data") as Bitmap
                addFileToList(photo)
            } else if (result.resultCode == Activity.RESULT_CANCELED && capturedImages.isEmpty()) {
                cancelAndFinish()
            }
        }

    private fun startRetrieveAction() {
        when (getRetrieverType()) {
            CAMERA -> startCameraAction()
            GALLERY -> startGalleryAction()
        }
    }

    private val takePictureResultFromMemory =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result?.data?.data != null) {
                    val selectedImage = result.data?.data
                    val filePath = FilePathUtil.getFilePath(this, selectedImage) ?: ""
                    capturedImages.add(Image(filePath))
                    setImageShow(File(filePath))
                } else {
                    if (result?.data?.clipData != null) {
                        val mClipData = result.data?.clipData
                        for (i in 0 until mClipData!!.itemCount) {
                            val item = mClipData.getItemAt(i)
                            val selectedImage = item.uri
                            val filePath = FilePathUtil.getFilePath(this, selectedImage) ?: ""
                            capturedImages.add(Image(filePath))
                            Glide.with(this).load(File(filePath)).into(views.showImage)
                        }
                    }
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED && capturedImages.isEmpty()) {
                cancelAndFinish()
            }
        }

    override val bindingInflater: (LayoutInflater) -> ActivityImageRetrieverBinding =
        ActivityImageRetrieverBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pickImagePermissionResult.launch(getPermissionsList())
        views.sendData.setOnClickListener {
            returnData()
        }
        views.getImage.setOnClickListener {
            startRetrieveAction()
        }
        views.imagesList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        views.imagesList.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        imagesAdapter = ImagesAdapter(capturedImages, this,this)
        views.imagesList.adapter = imagesAdapter
        views.closeIcon.setOnClickListener {
            cancelAndFinish()
        }
        if (intent?.hasExtra(IS_MESSAGE_AVAiLABLE) == true && intent?.extras?.getBoolean(IS_MESSAGE_AVAiLABLE) != true)
            views.sendMessage.hide()

    }


    private fun returnData() {
        imagesAdapter?.getSelectedItem()?.imageMessage =  views.sendMessage.text.toString()
        val returnIntent = Intent()
        returnIntent.putExtra(IMAGES, capturedImages)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addFileToList(bitmap: Bitmap) {
        val imageUri = getRealPathFromURI(getImageUri(bitmap))
        capturedImages// get system uri of bitmap
        capturedImages.add(Image(imageUri))
        imagesAdapter?.notifyDataSetChanged()
        imagesAdapter?.notifySelected(capturedImages.size - 1)
        setImageShow(File(imageUri)) //show image as selected
    }

    private fun getImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            contentResolver,
            inImage,
            "NFS-${RandomStringGenerator.getRandomString()}",
            null
        )
        return Uri.parse(path)
    }

    private fun getRealPathFromURI(uri: Uri): String {
        var path = ""
        if (contentResolver != null) {
            val cursor = contentResolver.query(uri, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    private fun setImageShow(file: File) {
        file.let { Glide.with(this).load(file).into(views.showImage)}
    }

    private fun startCameraAction() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureResultFromCamera.launch(takePictureIntent)
    }

    private fun startGalleryAction() {
        with(Intent(Intent.ACTION_PICK)) {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            action = Intent.ACTION_GET_CONTENT
            takePictureResultFromMemory.launch(this)
        }
    }


    override fun onItemClick(clickedItem: Image) {
        setImageShow(File(clickedItem.imageUrl))
        mLastSelected?.imageMessage =  views.sendMessage.text.toString()
        views.sendMessage.setText(clickedItem.imageMessage)
        mLastSelected = clickedItem
    }

    override fun onDeleteItemClick(position: Int) {
        val selected = imagesAdapter?.getSelectedItem()
        if (selected == capturedImages.getIfExists(position)) {
            when {
                capturedImages.getIfExists(position - 1) != null -> notifyImage(position - 1)
                capturedImages.getIfExists(position + 1) != null -> notifyImage(position + 1)
                else -> cancelAndFinish()
            }
        }
        capturedImages.removeIfExists(position)?.let {
            imagesAdapter?.notifyItemRemoved(position)
        }
    }

    private fun getPermissionsList(): Array<String> {
        return when (getRetrieverType()) {
            CAMERA -> pickImageFromCameraPermissions
            GALLERY -> pickImageFromMemoryPermissions
            else -> pickImageFromMemoryPermissions
        }
    }

    private fun notifyImage(position: Int) {
        capturedImages.getIfExists(position)?.let { image ->
            setImageShow(File(image.imageUrl)) //show image as selected
            imagesAdapter?.notifySelected(position)
        }
    }

    private fun cancelAndFinish() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun getRetrieverType() = intent?.extras?.getInt(IMAGE_RETRIEVER_TYPE)

    companion object {
        const val IMAGE_RETRIEVER_TYPE = "imagetreriever_type"
        const val CAMERA = 1
        const val GALLERY = 2
        const val IMAGES = "images"
        const val IS_MESSAGE_AVAiLABLE = "is_message_available"
    }


}