package az.needforspeak.ui.unregister

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import az.needforspeak.R
import az.needforspeak.base.BaseActivity
import az.needforspeak.base.BaseFragment
import az.needforspeak.databinding.FragmentRegistrationBinding
import az.needforspeak.model.local.Image
import az.needforspeak.model.local.NFSFile
import az.needforspeak.model.remote.auth.request.RegistrationRequestModel
import az.needforspeak.ui.image_retriever.ImageRetrieverActivity
import az.needforspeak.utils.*
import az.needforspeak.utils.helpers.MainSharedPrefs
import az.needforspeak.utils.image.ImageHelper
import az.needforspeak.utils.type_defs.FileExtensionTypes
import az.needforspeak.utils.type_defs.FolderTypes
import az.needforspeak.view_model.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class RegistrationFragment :
    BaseFragment<FragmentRegistrationBinding>(FragmentRegistrationBinding::inflate),
    View.OnClickListener,
    TextWatcher {

    private var plateNumber = ""
    private var redId = 0
    private var params = ""
    private val viewModel: LoginViewModel by viewModel()

    private val pickImagePermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var mSelectedImages: ArrayList<Image>? = null
    private val pickImagePermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            if (result?.all { it.value == true } == true) {
                pickImages()
            }
        }

    private val takePictureResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { data ->
            if (data.resultCode == Activity.RESULT_OK) {
                mSelectedImages = data.data?.extras?.getParcelableArrayList<Image>(
                    ImageRetrieverActivity.IMAGES
                )
                views.certificateCheck.visibility = View.VISIBLE
                checkInputs()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initRegisterApi()



        arguments?.let {
            redId = it.getInt("reqId") ?: 0
            params = it.getString("params") ?: ""
            plateNumber = it.getString("plateNumber") ?: ""
            views.plateInclude.plateEditText.setText(plateNumber)

            if (params.isNotEmpty()){
                views.firstStepLayout.visibility = View.GONE
                findNavController().navigate(R.id.checkRegisterFragment, bundleOf("isFromDeepLink" to true), getNavOptions())
            }

            if (redId!=0){
                views.firstStepLayout.visibility = View.GONE
                findNavController().navigate(R.id.checkRegisterFragment, bundleOf("reqId" to redId), getNavOptions())
            }
        }

    }


    private fun checkInputs() {
        if (views.plateInclude.plateEditText.text.toString().length >= 9 &&
            !mSelectedImages.isNullOrEmpty() &&
            views.phoneNumberInput.text.toString().length > 10) {
            views.submitRegistration.isEnabled = true
            views.submitRegistration.alpha = 1.0F
        }
        else{
            views.submitRegistration.isEnabled = false
            views.submitRegistration.alpha = 0.5F
        }
    }


    private fun initViews() {
        views.plateInclude.plateEditText.addTextChangedListener(
            MaskFormatter(
                "99_AA_999",
                views.plateInclude.plateEditText
            )
        )
        views.backArrow.setOnClickListener(this)
        views.getCertificatePhotos.setOnClickListener(this)
        views.submitRegistration.setOnClickListener(this)
        views.phoneNumberInput.addTextChangedListener(this)

        views.plateInclude.plateEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (views.plateInclude.plateEditText.text.length + 1 === 3 || views.plateInclude.plateEditText.text.length + 1 === 6) {
                    if (before - count < 0) {
                        views.plateInclude.plateEditText.setText(views.plateInclude.plateEditText.text.toString() + "-")
                        views.plateInclude.plateEditText.setSelection(views.plateInclude.plateEditText.text.length)
                    }
                }
                checkInputs()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

    }

    fun showCertificateGuideDialog() {
        val builder = AlertDialog.Builder(requireContext()).setCancelable(false)
        val view =
            LayoutInflater.from(requireContext()).inflate(R.layout.certificate_guide_layout, null)
        builder.setView(view)
        val infoCancelBtn: Button = view.findViewById(R.id.btnCancel)
        val infoOkBtn: Button = view.findViewById(R.id.btnOk)
        val alert = builder.create()
        infoCancelBtn.setOnClickListener {
            alert.dismiss()
        }
        infoOkBtn.setOnClickListener {
            getCertificateFiles()
            alert.dismiss()
        }
        alert.show()
    }

    private fun pickImages() {
        val intent = Intent(requireContext(), ImageRetrieverActivity::class.java)
        intent.putExtra(ImageRetrieverActivity.IMAGE_RETRIEVER_TYPE, ImageRetrieverActivity.GALLERY)
        intent.putExtra(ImageRetrieverActivity.IS_MESSAGE_AVAiLABLE, false)
        takePictureResult.launch(intent)
    }


    private fun getCertificateFiles() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissionToPickImages()
        } else {
            pickImages()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun checkPermissionToPickImages() {
        pickImagePermissionResult.launch(pickImagePermissions)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.backArrow -> {
                requireActivity().onBackPressed()
            }

            R.id.getCertificatePhotos -> {
                showCertificateGuideDialog()
            }

            R.id.submitRegistration -> {
                createRegistrationRequest()
            }
        }
    }

    private fun createRegistrationRequest() {
        BaseActivity.loadingUp()
        mSelectedImages?.let {
            val images = it.map { image ->
                val fileName = RandomStringGenerator.getRandomString()
                val file = File(image.imageUrl)
                NFSFile(fileName, FolderTypes.IMAGES, FileExtensionTypes.JPG, file)
            }
            ImageHelper.uploadMultipleFiles(requireContext(), images) { result ->
                if (result) {
                    sendSubscribeRequestToServer(images.map { nfsFile ->
                        nfsFile.fileName
                    })
                }
            }
        }
    }

    private fun initRegisterApi() {
        viewModel.registerResponse.observe(viewLifecycleOwner){
            if (it.data!=null){
                MainSharedPrefs(requireContext(),SharedTypes.USERDATA).set("REG_ID",it.data.request_id)
                findNavController().navigate(R.id.checkRegisterFragment,null, getNavOptions())
            }
        }
    }

    private fun sendSubscribeRequestToServer(files: List<String>) {
        val phone = "0"+views.phoneNumberInput.text.toString().replace(" ","")
        val registerRequest = RegistrationRequestModel(
            user_id = views.plateInclude.plateEditText.text.toString(),
            phone_number =  phone,
            documentPaths = files
        )
        viewModel.sendRegisterRequest(registerRequest)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
        when (s.hashCode()) {
            views.phoneNumberInput.text.hashCode() -> {
                views.phoneNumberInput.removeTextChangedListener(this)
                var editedphone =
                    UtilFuntions.formatterPaymentPhone(views.phoneNumberInput.text.toString(), 2)
                s?.replace(0, s.length, editedphone)
                views.phoneNumberInput.addTextChangedListener(this)
                checkInputs()
            }
        }

    }


}