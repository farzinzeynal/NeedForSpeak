package az.needforspeak.ui.register.post

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import az.needforspeak.R
import az.needforspeak.base.BaseActivity
import az.needforspeak.databinding.ActivityCreatePostBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_create_post.*
import java.io.File

class AddPostActivity : BaseActivity<ActivityCreatePostBinding>(),  View.OnClickListener {
    override val bindingInflater: (LayoutInflater) -> ActivityCreatePostBinding = ActivityCreatePostBinding::inflate

    var viewModel: GroupsViewModel? = null
    private var selectedRoomJid: String? = null
    private var mSelectedImages: ArrayList<Image>? = null
    private val takePictureResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { data ->
        if (data.resultCode == Activity.RESULT_OK) {
            mSelectedImages = data.data?.extras?.getParcelableArrayList<Image>(ImageRetrieverActivity.IMAGES)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(GroupsViewModel::class.java)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        selectedRoomJid = intent.getStringExtra(SELECTED_ROOM_JID)
        sendPostButton.setOnClickListener(this)
        startCamera.setOnClickListener(this)
        startGallery.setOnClickListener(this)
        closeIcon.setOnClickListener(this)
        showUserInfo()

    }

    private fun checkMemoryPermission() {
        val intent = Intent(this, ImageRetrieverActivity::class.java)
        intent.putExtra(ImageRetrieverActivity.IMAGE_RETRIEVER_TYPE, ImageRetrieverActivity.GALLERY)
        intent.putExtra(ImageRetrieverActivity.IS_MESSAGE_AVAiLABLE, false)
        takePictureResult.launch(intent)
    }

    private fun checkCameraPermission() {
        val intent = Intent(this, ImageRetrieverActivity::class.java)
        intent.putExtra(ImageRetrieverActivity.IMAGE_RETRIEVER_TYPE, ImageRetrieverActivity.CAMERA)
        intent.putExtra(ImageRetrieverActivity.IS_MESSAGE_AVAiLABLE, false)
        takePictureResult.launch(intent)
    }

    private fun sendPost() {
        val postId = UtilityFunctions.getCurrentTimeStamp()
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
        }
    }

    private fun sendPostToServer(postBody: PostBody) {
        selectedRoomJid?.let {
            viewModel?.sendPost(postBody)
            showToast(getString(R.string.send_post_have_sent))
            finish()
        }
    }

    private fun showUserInfo() {
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
    }

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
            closeIcon -> finish()
        }
    }


    companion object{
        const val SELECTED_ROOM_JID = "selectedRoomJid"
    }
}