package az.needforspeak.ui.register.settings

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import az.needforspeak.base.BaseActivity
import az.needforspeak.base.BaseFragment
import az.needforspeak.databinding.FragmentChangeYourCarBinding
import az.needforspeak.model.local.Image
import az.needforspeak.utils.MaskFormatter
import java.io.FileNotFoundException

class ChangeYourCarFragment : BaseFragment<FragmentChangeYourCarBinding>(FragmentChangeYourCarBinding::inflate) {

    var imageUriPost: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        views.plateInclude.plateEditText.addTextChangedListener(MaskFormatter("99_AA_999", views.plateInclude.plateEditText))

        views.plateInclude.plateEditText.doOnTextChanged { text, start, before, count ->
            checkButton()
        }

        views.plateInclude.plateEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (views.plateInclude.plateEditText.text.length + 1 === 3 || views.plateInclude.plateEditText.text.length + 1 === 6) {
                    if (before - count < 0) {
                        views.plateInclude.plateEditText.setText(views.plateInclude.plateEditText.text.toString() + "-")
                        views.plateInclude.plateEditText.setSelection(views.plateInclude.plateEditText.text.length)
                    }
                }
//                if(views.plateInclude.plateEditText.text.length < 3) {
//                    views.plateInclude.plateEditText.inputType = InputType.TYPE_CLASS_NUMBER
//                }else if(views.plateInclude.plateEditText.text.length < 6) {
//                    views.plateInclude.plateEditText.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
//                }else {
//                    views.plateInclude.plateEditText.inputType = InputType.TYPE_CLASS_NUMBER
//                }
//                views.plateInclude.plateEditText.setFilters(arrayOf<InputFilter>(AllCaps()))

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {

            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        views.addFriendBtn.setOnClickListener{

        }

        views.imageDoc.setOnClickListener {
            galleryLauncher.launch("image/*")
        }



    }

    var galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            if (imageUri != null) {
                try {
                    imageUriPost = imageUri
                    views.imageDoc.visibility = View.VISIBLE
                    views.imageDoc.setImageURI(imageUri)
                    checkButton()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }


    fun checkButton() {
        if(views.plateInclude.plateEditText.text.length == 9 && imageUriPost!=null) {
            views.addFriendBtn.alpha = 1f
            views.addFriendBtn.isEnabled = true
        }else {
            views.addFriendBtn.alpha = 0.6f
            views.addFriendBtn.isEnabled = false
        }
    }
}