package az.needforspeak.component

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import az.needforspeak.R
import az.needforspeak.base.BaseBottomSheetDialog
import az.needforspeak.databinding.PhotoSelectBottomSheetContentBinding

class PhotoSelectBottomSheet: BaseBottomSheetDialog<PhotoSelectBottomSheetContentBinding>(PhotoSelectBottomSheetContentBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }



    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data

        }
    }
}