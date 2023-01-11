package az.needforspeak.ui.register

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import az.needforspeak.R
import az.needforspeak.base.BaseActivity
import az.needforspeak.base.BaseFragment
import az.needforspeak.component.PhotoSelectBottomSheet
import az.needforspeak.databinding.FragmentAccountBinding
import az.needforspeak.model.local.Image
import az.needforspeak.model.remote.auth.response.ProfileResponseModel
import az.needforspeak.ui.image_retriever.ImageRetrieverActivity
import az.needforspeak.ui.unregister.UnregisterActivity
import az.needforspeak.utils.AuthUtils
import az.needforspeak.utils.SessionManager
import az.needforspeak.utils.UtilFuntions
import az.needforspeak.view_model.AccountViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : BaseFragment<FragmentAccountBinding>(FragmentAccountBinding::inflate),
    View.OnFocusChangeListener, View.OnClickListener {

    private var friendUserId = ""

    private val viewModel: AccountViewModel by viewModel()
    var userId = SessionManager.getCurrentUserId()

    private val pickImagePermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
    )

    private var mSelectedImages: ArrayList<Image>? = null

    //visibilities
    private var nameVisibility = false
    private var surenameVisibility = false
    private var phoneNumberVisibility = false
    private var statusVisibility = false
    private var careerVisibility = false
    private var educationVisibility = false
    private var interestesVisibility = false


    private val getPicturesResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { d ->
            if (d.resultCode == Activity.RESULT_OK) {
                mSelectedImages = d.data?.extras?.getParcelableArrayList<Image>(
                    ImageRetrieverActivity.IMAGES
                )
                Log.i("mSelectedImages",mSelectedImages?.size.toString() +" "+ (mSelectedImages?.get(0)?.imageUrl ?: ""))
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initProfileApi()


        arguments?.let {
            friendUserId = it.getString("friendUserId") ?: ""
            viewModel.getUserProfileData(friendUserId)
            setInputsEnabled(false)
            views.plateLayout.plateNum.text = friendUserId
        } ?: run {
            viewModel.getUserProfileData(userId)
            setInputsEnabled(true)
            views.plateLayout.plateNum.text = userId
        }
    }

    private fun downLoadUserProfileImageByUserID(userId: String) {
        val url = "https://api.needforspeak.xyz/profile/$userId/profile-picture"
        val auth: LazyHeaders = LazyHeaders.Builder() // can be cached in a field and reused
            .addHeader("Authorization", AuthUtils.token)
            .build()

        Glide.with(this).load(GlideUrl(url,auth)).into(views.userImage)
    }

    private fun downLoadUserProfileImageByDocumentID(docId: String) {
        val url = "https://api.needforspeak.xyz/document/$docId"
        val auth: LazyHeaders = LazyHeaders.Builder() // can be cached in a field and reused
            .addHeader("Authorization", AuthUtils.token)
            .build()

        Glide.with(this).load(GlideUrl(url,auth)).into(views.userImage)
    }






    private fun initViews() {
        views.inputName.onFocusChangeListener=this
        views.inputSurename.onFocusChangeListener=this
        views.inputPhoneNumber.onFocusChangeListener=this
        views.inputStatus.onFocusChangeListener=this
        views.inputCareer.onFocusChangeListener=this
        views.inputEducation.onFocusChangeListener=this
        views.inputInterestes.onFocusChangeListener=this

        views.toogleName.setOnClickListener(this)
        views.toogleSurename.setOnClickListener(this)
        views.tooglePhoneNumber.setOnClickListener(this)
        views.toogleStatus.setOnClickListener(this)
        views.toogleCareer.setOnClickListener(this)
        views.toogleEducation.setOnClickListener(this)
        views.toogleInterestes.setOnClickListener(this)
        views.userImage.setOnClickListener(this)
        views.addProfileImage.setOnClickListener(this)
    }

    private fun initProfileApi() {
        viewModel.profileResponse.observe(viewLifecycleOwner) {
            BaseActivity.loadingDown()
            if (it.data != null) {
                setDatas(it.data)
                setVisibilities(it.data)
            }
        }
    }

    private fun setVisibilities(data: ProfileResponseModel) {
        data.name?.let { nameVisibility = it.visible  }
        data.surname?.let { surenameVisibility = it.visible }
        data.phone_number?.let { phoneNumberVisibility = it.visible}
        data.about?.let { statusVisibility = it.visible}
        data.career?.let { careerVisibility = it.visible}
        data.education?.let { educationVisibility = it.visible}
        data.interests?.let { interestesVisibility = it.visible}

        setVisibilitiToogles(data)
    }

    private fun setVisibilitiToogles(data: ProfileResponseModel) {
        data.name?.let { views.toogleName.setImageResource(if (nameVisibility) R.drawable.ic_eye else R.drawable.ic_eye_not)}
        data.surname?.let { views.toogleSurename.setImageResource(if (surenameVisibility) R.drawable.ic_eye else R.drawable.ic_eye_not) }
        data.phone_number?.let { views.tooglePhoneNumber.setImageResource(if (phoneNumberVisibility) R.drawable.ic_eye else R.drawable.ic_eye_not) }
        data.about?.let { views.toogleStatus.setImageResource(if (statusVisibility) R.drawable.ic_eye else R.drawable.ic_eye_not) }
        data.career?.let { views.toogleCareer.setImageResource(if (careerVisibility) R.drawable.ic_eye else R.drawable.ic_eye_not) }
        data.education?.let { views.toogleEducation.setImageResource(if (educationVisibility) R.drawable.ic_eye else R.drawable.ic_eye_not) }
        data.interests?.let { views.toogleInterestes.setImageResource(if (interestesVisibility) R.drawable.ic_eye else R.drawable.ic_eye_not) }
    }

    private fun setDatas(data: ProfileResponseModel) {
        data.name?.let { setAccountData(views.inputName, data.name.value, data.name.visible) }
        data.surname?.let { setAccountData(views.inputSurename, data.surname.value, data.surname.visible) }
        data.phone_number?.let { setAccountData(views.inputPhoneNumber, data.phone_number.value, data.phone_number.visible) }
        data.about?.let { setAccountData(views.inputStatus, data.about.value, data.about.visible) }
        data.career?.let { setAccountData(views.inputCareer, data.career.value, data.career.visible) }
        data.education?.let { setAccountData(views.inputEducation, data.education.value, data.education.visible) }
        data.interests?.let { setAccountData(views.inputInterestes, data.interests.value, data.interests.visible) }
        data.profile_pictures?.let { downLoadUserProfileImageByDocumentID(data.profile_pictures[0]) }
    }

    private fun updateProfileData(viewId: Int, key: String, value: String, isVisible: Boolean) {
        when (viewId) {
            R.id.inputName -> {
                viewModel.updateProfileData(userId, key, value, isVisible)
            }
            R.id.inputSurename -> {
                viewModel.updateProfileData(userId, key, value, isVisible)
            }
            R.id.inputPhoneNumber -> {
                viewModel.updateProfileData(userId, key, value, isVisible)
            }
            R.id.inputCareer -> {
                viewModel.updateProfileData(userId, key, value, isVisible)
            }
            R.id.inputEducation -> {
                viewModel.updateProfileData(userId, key, value, isVisible)
            }
            R.id.inputInterestes -> {
                viewModel.updateProfileData(userId, key, value, isVisible)
            }
            R.id.inputStatus -> {
                viewModel.updateProfileData(userId, key, value, isVisible)
            }
        }
    }

    private fun setAccountData(view: TextInputEditText, text: String, isVisible: Boolean) {
        view.setText(text)
        if (isVisible){
            view.visibility = View.VISIBLE
        }
        else{
            view.visibility = if (friendUserId.isEmpty()) View.VISIBLE else View.INVISIBLE
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            requireActivity().window.statusBarColor =
                resources.getColor(R.color.background_two_color)
        }
    }


    private fun showImagePickDialog() {

        val bsheet = PhotoSelectBottomSheet()
        bsheet.show(requireActivity().supportFragmentManager, PhotoSelectBottomSheet.TAG)

        Handler().post {
            bsheet.views.takeCamera.setOnClickListener {
                bsheet.dismiss()
                val intent = Intent(requireContext(), ImageRetrieverActivity::class.java)
                intent.putExtra(ImageRetrieverActivity.IMAGE_RETRIEVER_TYPE, ImageRetrieverActivity.CAMERA)
                intent.putExtra(ImageRetrieverActivity.IS_MESSAGE_AVAiLABLE, false)
                getPicturesResult.launch(intent)

            }
            bsheet.views.takeGallery.setOnClickListener {
                bsheet.dismiss()
                val intent = Intent(requireContext(), ImageRetrieverActivity::class.java)
                intent.putExtra(ImageRetrieverActivity.IMAGE_RETRIEVER_TYPE, ImageRetrieverActivity.GALLERY)
                intent.putExtra(ImageRetrieverActivity.IS_MESSAGE_AVAiLABLE, false)
                getPicturesResult.launch(intent)
            }
        }
    }


    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (!hasFocus)
            when (view) {
                views.inputName -> {
                    updateProfileData(views.inputName.id, "name", views.inputName.text.toString(),nameVisibility)
                }
                views.inputSurename -> {
                    updateProfileData(views.inputSurename.id, "surname", views.inputSurename.text.toString(),surenameVisibility)
                }
                views.inputPhoneNumber -> {
                    updateProfileData(views.inputPhoneNumber.id, "phone-number", views.inputPhoneNumber.text.toString(),phoneNumberVisibility)
                }
                views.inputStatus -> {
                    updateProfileData(views.inputStatus.id, "about", views.inputStatus.text.toString(),statusVisibility)
                }
                views.inputEducation -> {
                    updateProfileData(views.inputEducation.id, "education", views.inputEducation.text.toString(),educationVisibility)
                }
                views.inputCareer -> {
                    updateProfileData(views.inputCareer.id, "career", views.inputCareer.text.toString(),careerVisibility)
                }
                views.inputInterestes -> {
                    updateProfileData(views.inputInterestes.id, "interests", views.inputInterestes.text.toString(),interestesVisibility)
                }
            }
    }


    var galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
        if (imageUri != null) {
            views.userImage.setImageURI(imageUri)
        }
    }
    var cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val photo = result.data?.getExtras()?.get("data") as Bitmap
                val uri = UtilFuntions.bitmapToFile(requireActivity().applicationContext,photo)
                views.userImage.setImageURI(uri)
            }
        }

    private var pickImagePermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            if (result?.all { it.value == true } == true) {
               ///val intent = Intent(requireActivity(), ImageRetrieverActivity::class.java)
               ///intent.putExtra(ImageRetrieverActivity.IMAGE_RETRIEVER_TYPE, ImageRetrieverActivity.GALLERY)
               ///intent.putExtra(ImageRetrieverActivity.IS_MESSAGE_AVAiLABLE, false)
               ///getPicturesResult.launch(intent)
                showImagePickDialog()
            }
        }


    override fun onClick(view: View?) {
        when(view?.id){
            R.id.toogleName ->{
                if (nameVisibility){
                    views.toogleName.setImageResource(R.drawable.ic_eye_not)
                    nameVisibility=false
                }
                else{
                    views.toogleName.setImageResource(R.drawable.ic_eye)
                    nameVisibility=true
                }
                viewModel.updateProfileData(userId, "name", views.inputName.text.toString(),nameVisibility)
            }

            R.id.toogleSurename ->{
                if (surenameVisibility){
                    views.toogleSurename.setImageResource(R.drawable.ic_eye_not)
                    surenameVisibility=false
                }
                else{
                    views.toogleSurename.setImageResource(R.drawable.ic_eye)
                    surenameVisibility=true
                }
                viewModel.updateProfileData(userId, "surname", views.inputSurename.text.toString(),surenameVisibility)
            }

            R.id.tooglePhoneNumber ->{
                if (phoneNumberVisibility){
                    views.tooglePhoneNumber.setImageResource(R.drawable.ic_eye_not)
                    phoneNumberVisibility=false
                }
                else{
                    views.tooglePhoneNumber.setImageResource(R.drawable.ic_eye)
                    phoneNumberVisibility=true
                }
                viewModel.updateProfileData(userId, "phone-number", views.inputPhoneNumber.text.toString(),phoneNumberVisibility)
            }

            R.id.toogleStatus ->{
                if (statusVisibility){
                    views.toogleStatus.setImageResource(R.drawable.ic_eye_not)
                    statusVisibility=false
                }
                else{
                    views.toogleStatus.setImageResource(R.drawable.ic_eye)
                    statusVisibility=true
                }
                viewModel.updateProfileData(userId, "about", views.inputStatus.text.toString(),statusVisibility)
            }

            R.id.toogleCareer ->{
                if (careerVisibility){
                    views.toogleCareer.setImageResource(R.drawable.ic_eye_not)
                    careerVisibility=false
                }
                else{
                    views.toogleCareer.setImageResource(R.drawable.ic_eye)
                    careerVisibility=true
                }
                viewModel.updateProfileData(userId, "career", views.inputCareer.text.toString(),careerVisibility)
            }

            R.id.toogleEducation ->{
                if (educationVisibility){
                    views.toogleEducation.setImageResource(R.drawable.ic_eye_not)
                    educationVisibility=false
                }
                else{
                    views.toogleEducation.setImageResource(R.drawable.ic_eye)
                    educationVisibility=true
                }
                viewModel.updateProfileData(userId, "education", views.inputEducation.text.toString(),educationVisibility)
            }

            R.id.toogleInterestes ->{
                if (interestesVisibility){
                    views.toogleInterestes.setImageResource(R.drawable.ic_eye_not)
                    interestesVisibility=false
                }
                else{
                    views.toogleInterestes.setImageResource(R.drawable.ic_eye)
                    interestesVisibility=true
                }
                viewModel.updateProfileData(userId, "interests", views.inputInterestes.text.toString(),interestesVisibility)
            }

            R.id.userImage ->{
                pickImagePermissionResult.launch(pickImagePermissions)
            }

            R.id.addProfileImage ->{
               views.userImage.performClick()
            }
        }

    }

    fun setInputsEnabled(isEnabled: Boolean){
        if (isEnabled){
            views.nameLayout.isEnabled=true
            views.sureNameLayout.isEnabled=true
            views.phoneNumberLayout.isEnabled=true
            views.statusLayout.isEnabled=true
            views.careerLayout.isEnabled=true
            views.educationLayout.isEnabled=true
            views.inputInterestes.isEnabled=true
            views.toogleName.isEnabled=true
            views.toogleSurename.isEnabled=true
            views.tooglePhoneNumber.isEnabled=true
            views.toogleStatus.isEnabled=true
            views.toogleCareer.isEnabled=true
            views.toogleEducation.isEnabled=true
            views.toogleInterestes.isEnabled=true
        }
        else{
            views.inputName.isEnabled=false
            views.inputSurename.isEnabled=false
            views.inputPhoneNumber.isEnabled=false
            views.inputStatus.isEnabled=false
            views.inputCareer.isEnabled=false
            views.inputEducation.isEnabled=false
            views.inputInterestes.isEnabled=false
            views.toogleName.isEnabled=false
            views.toogleSurename.isEnabled=false
            views.tooglePhoneNumber.isEnabled=false
            views.toogleStatus.isEnabled=false
            views.toogleCareer.isEnabled=false
            views.toogleEducation.isEnabled=false
            views.toogleInterestes.isEnabled=false
        }
    }

    override fun onPause() {
        super.onPause()
    }
}