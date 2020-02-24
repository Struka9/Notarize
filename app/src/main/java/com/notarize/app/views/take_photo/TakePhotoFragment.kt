package com.notarize.app.views.take_photo

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.notarize.app.R
import com.notarize.app.ext.createLoadingDialog
import kotlinx.android.synthetic.main.fragment_photo.*
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ViewModelParameters
import org.koin.androidx.viewmodel.getViewModel

class TakePhotoFragment : Fragment() {

    private var imageCapture: ImageCapture? = null

    // WHY: We want to get a shared viewmodel to access the URI in both fragments
    private val takePhotoViewModel: TakePhotoViewModel by lazy {
        return@lazy getKoin().getViewModel(
            ViewModelParameters(
                TakePhotoViewModel::class,
                owner = requireActivity()
            )
        )
    }

    lateinit var loadingDialog: Dialog

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10

        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadingDialog = view.context.createLoadingDialog()

        takePhotoViewModel.isDoingBackgroundWork.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                loadingDialog.show()
            } else {
                loadingDialog.hide()
            }
        })

        takePhotoViewModel
            .photoFileUri
            .observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    findNavController().navigate(R.id.photoPreview)
                }
            })

        bt_take_photo.setOnClickListener {
            takePhotoViewModel.savePictureToFile(imageCapture)
        }

        requestPermissions()
    }

    private fun requestPermissions() {
        if (allPermissionsGranted()) {
            textureView.post { startCamera() }
        } else {
            requestPermissions(
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                textureView.post { startCamera() }
            } else {
                requireActivity().finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun updateTransform() {
        val matrix = Matrix()

        val centerX = textureView.width / 2f
        val centerY = textureView.height / 2f

        val rotationDegrees = when (textureView.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        textureView.setTransform(matrix)
    }

    private fun startCamera() {
        CameraX.unbindAll()

        val preview = createPreviewUseCase()

        preview.setOnPreviewOutputUpdateListener {
            val parent = textureView.parent as ViewGroup
            parent.removeView(textureView)
            parent.addView(textureView, 0)

            textureView.surfaceTexture = it.surfaceTexture
            updateTransform()
        }

        imageCapture = createCaptureUseCase()
        CameraX.bindToLifecycle(this, preview, imageCapture)
    }

    private fun createPreviewUseCase(): Preview {
        val previewConfig = PreviewConfig.Builder().apply {
            setTargetRotation(textureView.display.rotation)

        }.build()

        return Preview(previewConfig)
    }

    private fun createCaptureUseCase(): ImageCapture {
        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .apply {
                setLensFacing(CameraX.LensFacing.BACK)
                setTargetRotation(textureView.display.rotation)
                setCaptureMode(ImageCapture.CaptureMode.MAX_QUALITY)
            }

//        applyExtensions(imageCaptureConfig)
        return ImageCapture(imageCaptureConfig.build())
    }
}