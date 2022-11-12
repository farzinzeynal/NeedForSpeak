package az.needforspeak.ui.register

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.storage.StorageManager
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import az.needforspeak.base.BaseActivity
import az.needforspeak.component.AudioRecordView
import az.needforspeak.component.PhotoSelectBottomSheet
import az.needforspeak.component.adapter.MessageAdapter
import az.needforspeak.component.adapter.MessagesAdapter
import az.needforspeak.databinding.ActivityMessagingBinding
import az.needforspeak.model.local.MESSAGE_SENDER
import az.needforspeak.model.local.MESSAGE_TYPE
import az.needforspeak.model.local.Message
import az.needforspeak.model.local.MessageModel
import az.needforspeak.view_model.MessagingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.*
import java.util.*


class MessagingActivity : BaseActivity<ActivityMessagingBinding>(),
    AudioRecordView.RecordingListener {
    override val bindingInflater: (LayoutInflater) -> ActivityMessagingBinding =
        ActivityMessagingBinding::inflate

    private lateinit var audioRecordView: AudioRecordView
    private lateinit var messageAdapter: MessageAdapter
    private var recorder: MediaRecorder? = null
    private var fileName: String = ""
    private var player: MediaPlayer? = null
    private var isPlay = false
    private var isRecording = false
    private lateinit var linearLayoutManager: LinearLayoutManager
    val list = mutableListOf<MessageModel>()
    private lateinit var adapter: MessagesAdapter
    private val viewModel: MessagingViewModel by viewModel()

    override fun onPause() {
        super.onPause()
        if(isRecording) {
            onRecordingCanceled()
        }
        audioRecordView
    }
    override fun onViewBindingCreated(savedInstanceState: Bundle?) {
        super.onViewBindingCreated(savedInstanceState)
        linearLayoutManager = LinearLayoutManager(this)
        audioRecordView = AudioRecordView() { it -> onPressCallback(it) }
        audioRecordView.initView(views.layoutMain)
        audioRecordView.recordingListener = this
        views.backArrow.setOnClickListener { onBackPressed() }
        views.messageRecyclerView.layoutManager = linearLayoutManager



//        list.add(MessageModel(message = "Salam. Bu vaxt yazma))", date = "05:22", type = MESSAGE_TYPE.TEXT, img = null, voice = null, MESSAGE_SENDER.FROM))
//        list.add(MessageModel(message = "HJASDJJSANDJNJNJASDJNJASDNJANJSDJNANSJDADJNASJDNJ", date = "05:22", type = MESSAGE_TYPE.TEXT, img = null, voice= null, MESSAGE_SENDER.TO))
//        list.add(MessageModel(message = null, date = "05:22", type = MESSAGE_TYPE.VOICE, img = null, voice = "/storage/emulated/0/Android/data/az.needforspeak/files/1651495670760_record.3gp", MESSAGE_SENDER.TO))
//        list.add(MessageModel(message = null, date = "05:22", type = MESSAGE_TYPE.VOICE, img = null, voice = "/storage/emulated/0/Android/data/az.needforspeak/files/1651495670760_record.3gp", MESSAGE_SENDER.TO))
//        list.add(MessageModel(message = null, date = "05:22", type = MESSAGE_TYPE.VOICE, img = null, voice = "/storage/emulated/0/Android/data/az.needforspeak/files/1651495670760_record.3gp", MESSAGE_SENDER.TO))
//        list.add(MessageModel(message = null, date = "05:22", type = MESSAGE_TYPE.PHOTO, img = "https://petkeen.com/wp-content/uploads/2021/09/Cute-Hedgehog-760x505.jpg", voice= null, MESSAGE_SENDER.TO))
//        list.add(MessageModel(message = "Qesheng qiz.Evlen))", date = "05:22", type = MESSAGE_TYPE.TEXT, img = null, voice= null, MESSAGE_SENDER.FROM))

        adapter = MessagesAdapter(this, list)
        views.messageRecyclerView.adapter = adapter
        viewModel.messages.observe(this) {
            it.forEach {
                list.add(MessageModel(message = it.message, date = it.date, type = MESSAGE_TYPE.TEXT, img = null, voice = null, MESSAGE_SENDER.FROM))
            }
            adapter.notifyDataSetChanged()
        }


        intent.getStringExtra("plateNumber")?.let { viewModel.initView(this, it) }
        val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter?.onDestroy()
    }


    fun onPressCallback(type: AudioRecordView.CLICK_TYPE) {
        when (type) {
            AudioRecordView.CLICK_TYPE.CAMERA -> {
                if (checkAndRequestPermissions(this)) {
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraLauncher.launch(takePicture)
                }
            }
            AudioRecordView.CLICK_TYPE.GALLERY -> {
                if (checkAndRequestPermissions(this)) {
                    chooseImage(this)
                }
            }

        }
    }

    val REQUEST_ID_MULTIPLE_PERMISSIONS = 101
    val REQUEST_ID_RECORD_PERMISSIONS = 102

    private fun chooseImage(context: Context) {
        val bsheet = PhotoSelectBottomSheet()
        bsheet.show(supportFragmentManager, PhotoSelectBottomSheet.TAG)

        Handler().post {
            bsheet.views.takeCamera.setOnClickListener {
                bsheet.dismiss()
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraLauncher.launch(takePicture)
            }
            bsheet.views.takeGallery.setOnClickListener {
                bsheet.dismiss()

                galleryLauncher.launch("image/*")
            }
        }
    }

    // function to check permission
    fun checkAndRequestPermissions(context: Activity?): Boolean {
        val WExtstorePermission = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val cameraPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                context, listPermissionsNeeded
                    .toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    fun checkAndRequestPermissionsVoice(context: Activity?): Boolean {

        val voicePermission = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.RECORD_AUDIO
        )
        val readPermission = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val writePermission = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val msPermission = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        )

        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (voicePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO)
        }
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (msPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                context, listPermissionsNeeded
                    .toTypedArray(),
                REQUEST_ID_RECORD_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> if (ContextCompat.checkSelfPermission(
                    this@MessagingActivity,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    applicationContext,
                    "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT
                )
                    .show()
            } else if (ContextCompat.checkSelfPermission(
                    this@MessagingActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    applicationContext,
                    "FlagUp Requires Access to Your Storage.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                chooseImage(this@MessagingActivity)
            }
            REQUEST_ID_RECORD_PERMISSIONS -> {
                if (ContextCompat.checkSelfPermission(
                        this@MessagingActivity,
                        Manifest.permission.RECORD_AUDIO
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(
                        applicationContext,
                        "Record permission problem", Toast.LENGTH_SHORT
                    )
                        .show()
                }else if (ContextCompat.checkSelfPermission(
                        this@MessagingActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(
                        applicationContext,
                        "Record READ permission problem", Toast.LENGTH_SHORT
                    )
                        .show()
                }else if (ContextCompat.checkSelfPermission(
                        this@MessagingActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(
                        applicationContext,
                        "Record WRITE permission problem", Toast.LENGTH_SHORT
                    )
                        .show()
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startRecording( System.currentTimeMillis().toString() + "_record")
                    }
                }
            }
        }
    }
    private var timeCurrent = System.currentTimeMillis()
    private fun startRecording(fName: String) {

        fileName = "${getExternalFilesDir(null)}/${fName}.3gp"
        Log.e("FFFFF", fileName)
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            try {
                prepare()
            } catch (e: IOException) {

            }
            start()
        }
        isRecording = true
    }

    @Nullable
    fun getInternalStorageDirectoryPath(context: Context): String? {
        val storageDirectoryPath: String?
        storageDirectoryPath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val storageManager = context.getSystemService(STORAGE_SERVICE) as StorageManager
            if (storageManager == null) {
                null //you can replace it with the Environment.getExternalStorageDirectory().getAbsolutePath()
            } else {
                storageManager.primaryStorageVolume.directory!!.absolutePath
            }
        } else {
            Environment.getExternalStorageDirectory().absolutePath
        }
        return storageDirectoryPath
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
            }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        isRecording = false
    }

    var galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            if (imageUri != null) {

                try {

                    list.add(MessageModel(message = null, date = "05:22", type = MESSAGE_TYPE.PHOTO, img = imageUri.toString(), voice = null, MESSAGE_SENDER.FROM))
                    views.messageRecyclerView.adapter?.notifyItemInserted(list.size - 1)
                    views.messageRecyclerView.scrollToPosition(list.size - 1)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }



            }
        }
    var cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val photo = result.data?.getExtras()?.get("data") as Bitmap
                val uri = bitmapToFile(photo)
                list.add(MessageModel(message = null, date = "05:22", type = MESSAGE_TYPE.PHOTO, img = uri.toString(), voice = null, MESSAGE_SENDER.FROM))
                views.messageRecyclerView.adapter?.notifyItemInserted(list.size - 1)
                views.messageRecyclerView.scrollToPosition(list.size - 1)
            }
        }

    private fun bitmapToFile(bitmap:Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir("Images",Context.MODE_PRIVATE)
        file = File(file,"${UUID.randomUUID()}.jpg")

        try{
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch (e:IOException){
            e.printStackTrace()
        }

        return Uri.parse(file.absolutePath)
    }

    private fun setListener() {
        audioRecordView.emojiView!!.setOnClickListener {
            audioRecordView.hideAttachmentOptionView()

        }

        audioRecordView.cameraView!!.setOnClickListener {
            audioRecordView.hideAttachmentOptionView()

        }

        audioRecordView.sendView!!.setOnClickListener {

            val msg = audioRecordView.messageView!!.text.toString().trim { it <= ' ' }
            audioRecordView.messageView!!.setText("")
            messageAdapter.add(Message(msg))
        }
    }

    override fun onRecordingStarted() {
        timeCurrent = System.currentTimeMillis()
        checkAndRequestPermissionsVoice(this)
    }

    override fun onRecordingLocked() {

    }

    override fun onSend(text: String) {
        list.add(MessageModel(message = text, date = "05:22", type = MESSAGE_TYPE.TEXT, img = null, voice = null, MESSAGE_SENDER.FROM))
        views.messageRecyclerView.adapter?.notifyItemInserted(list.size - 1)
        views.messageRecyclerView.scrollToPosition(list.size - 1)
    }

    override fun onRecordingCompleted() {
        if(System.currentTimeMillis() - 1000 > timeCurrent) {
            Handler().postDelayed({
                stopRecording()
                list.add(MessageModel(message = null, date = "05:22", type = MESSAGE_TYPE.VOICE, img = null, voice = fileName, MESSAGE_SENDER.FROM))
                views.messageRecyclerView.adapter?.notifyItemInserted(list.size - 1)
                views.messageRecyclerView.scrollToPosition(list.size - 1)
            }, 200)
        }else {
            Handler().postDelayed({
                stopRecording()
                val file = File(
                    fileName
                )
                file.delete()
            }, 500)
        }
    }

    override fun onRecordingCanceled() {
        stopRecording()
        val file = File(
            fileName
        )
        file.delete()
    }
}

//            val galleryintent = Intent(Intent.ACTION_GET_CONTENT, null)
//            galleryintent.type = "image/*"
//            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            val chooser = Intent(Intent.ACTION_CHOOSER)
//            chooser.putExtra(Intent.EXTRA_INTENT, galleryintent)
//            chooser.putExtra(Intent.EXTRA_TITLE, "Select from:")
//            val intentArray = arrayOf(cameraIntent)
//            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
//
//            galleryLauncher.launch(chooser)