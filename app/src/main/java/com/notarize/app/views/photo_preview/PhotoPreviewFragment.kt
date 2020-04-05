package com.notarize.app.views.photo_preview


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.notarize.app.R
import com.notarize.app.ext.createLoadingDialog
import com.notarize.app.views.take_photo.TakePhotoViewModel
import kotlinx.android.synthetic.main.fragment_photo_preview.*
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ViewModelParameters
import org.koin.androidx.viewmodel.getViewModel

/**
 * A simple [Fragment] subclass.
 */
class PhotoPreviewFragment : Fragment() {

    // We need to share the same TakePhotoViewModel
    private val takePhotoViewModel: TakePhotoViewModel by lazy {
        return@lazy getKoin().getViewModel(
            ViewModelParameters(
                TakePhotoViewModel::class,
                owner = requireActivity()
            )
        )
    }

    private lateinit var progressDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photo_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressDialog = requireContext().createLoadingDialog()

        takePhotoViewModel.photoFileUri.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                findNavController().popBackStack()
            } else {
                img_preview.setImageURI(it)
            }
        })

        // TODO: Maintain 2 different flags
        takePhotoViewModel.isDoingBackgroundWork.value = false

        takePhotoViewModel.isDoingBackgroundWork.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                progressDialog.show()
            } else {
                progressDialog.hide()
            }
        })

        bt_retake_photo.setOnClickListener {
            takePhotoViewModel.photoFileUri.value = null
        }
        bt_sign.setOnClickListener {
            takePhotoViewModel.submitDocument()
        }
    }
}
