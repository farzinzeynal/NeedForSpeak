package az.needforspeak.ui.register.post

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import az.needforspeak.R
import az.needforspeak.base.BaseActivity
import az.needforspeak.databinding.ActivityCreatePostBinding
import az.needforspeak.model.local.*
import az.needforspeak.utils.Constants
import az.needforspeak.utils.Extentions.showToast
import az.needforspeak.utils.UtilFuntions
import coil.load
import com.google.gson.Gson
import com.needforspeak.models.typedefs.FileExtensionTypes
import com.needforspeak.models.typedefs.FolderTypes
import com.needforspeak.ui.network.model.PostBody
import kotlinx.android.synthetic.main.activity_create_post.*
import java.io.*
import java.util.*

class AddCommentActivity : BaseActivity<ActivityCreatePostBinding>(), View.OnClickListener {
    override val bindingInflater: (LayoutInflater) -> ActivityCreatePostBinding =
        ActivityCreatePostBinding::inflate

    // var viewModel: GroupsViewModel? = null
    private var selectedRoomJid: String? = null


      private var mSelectedImages: ArrayList<Image>? = null
      /*private val takePictureResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { data ->
          if (data.resultCode == Activity.RESULT_OK) {
              mSelectedImages = data.data?.extras?.getParcelableArrayList<Image>(ImageRetrieverActivity.IMAGES)
          }
      }
  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //viewModel = ViewModelProvider(this).get(GroupsViewModel::class.java)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        selectedRoomJid = intent.getStringExtra(SELECTED_ROOM_JID)
        views.sendPostButton.setOnClickListener(this)
        views.startCamera.setOnClickListener(this)
        views.startGallery.setOnClickListener(this)
        views.closeIcon.setOnClickListener(this)
        // showUserInfo()

    }

    var galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            if (imageUri != null) {
                try {
                    mSelectedImages?.add(Image(imageUri.toString()))
                    views.postImage.visibility = View.VISIBLE
                    views.postImage.setImageURI(imageUri)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }

    var cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val photo = result.data?.getExtras()?.get("data") as Bitmap
                val uri = bitmapToFile(photo)
                mSelectedImages?.add(Image(uri.toString()))
                views.postImage.visibility = View.VISIBLE
                views.postImage.setImageURI(uri)
            }
        }

    private fun bitmapToFile(bitmap:Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file,"${UUID.randomUUID()}.jpg")

        try{
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }

        return Uri.parse(file.absolutePath)
    }

    private fun sendPostToServer() {
        selectedRoomJid?.let {
            //viewModel?.sendPost(postBody)
            showToast(getString(R.string.post_sent))
            finish()
        }
    }


    private fun checkMemoryPermission() {
        galleryLauncher.launch("image/*")
    }

    private fun checkCameraPermission() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(takePicture)
    }

    private fun sendPost() {
        if(!views.postSubject.text.toString().isNullOrEmpty() && !views.postBody.text.toString().isNullOrEmpty()){
            loadingUp()
            Handler().postDelayed({
                loadingDown()
                showToast(getString(R.string.post_sent))
                this@AddCommentActivity.finish()
            },3000)
        }

       /* mSelectedImages
         val postId = UtilFuntions.getCurrentTimeStamp()
         val post = PostBody(
             postId.toString(), "220222022${Constants.XMPP_HOST}",
             postSubject.text.toString(), postBody.text.toString(), selectedRoomJid ?: "ddddd", postId
         )
         mSelectedImages?.let {
             val images = it.map { image ->
                 val fileName = RandomStringGenerator.getRandomString()
                 val file = File(image.imageUrl)
                 NFSFile(fileName, FolderTypes.IMAGES, FileExtensionTypes.JPG, file)
             }
             val postImages = images.map { nfsFile ->
                 nfsFile.fileName
             }
             post.postImage = postImages
             ImageHelper.uploadMultipleFiles(this, images) { result ->
                 if (result) {
                     val postImages = images.map { nfsFile ->
                         nfsFile.fileName
                     }
                     post.postImage = postImages
                     sendPostToServer(post)
                 }
             }
         } ?: run {
             sendPostToServer(post)
         }*/
    }


    /* private fun showUserInfo() {
        val userId = SessionManager.getRegisteredUserId(this)
        userName.text = userId
        viewModel?.getUserDatByUserId(userId.fromStringToJid())?.observe(this, Observer {
            it?.additional?.userPhotos?.let {
                try {
                    val profileImage = Gson().fromJson<ArrayList<String?>>(it, ArrayList::class.java)?.getIfExists(0) ?: ""
                    val downloadFile = createFile(FileManager.generateCacheFolderByName(this, Constants.PROFILE_IMAGES), profileImage)
                    if (downloadFile.exists()) {
                        val myBitmap: Bitmap = BitmapFactory.decodeFile(downloadFile.absolutePath)
                        userImage?.setImageBitmap(myBitmap)
                    }
                } catch (e: Exception) {
                }
            }
        })
        ImageHelper.showProfileImage(userImage, userId ?: "")
    }*/

    private fun createFile(folderUrl: String, fileName: String): File {
        val folder = File(folderUrl)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return File(folder, fileName)
    }

    override fun onClick(view: View?) {
        when (view) {
            startGallery -> checkMemoryPermission()
            startCamera -> checkCameraPermission()
            sendPostButton -> sendPost()
            views.closeIcon -> this@AddCommentActivity.finish()
        }
    }


    companion object {
        const val SELECTED_ROOM_JID = "selectedRoomJid"
    }
}