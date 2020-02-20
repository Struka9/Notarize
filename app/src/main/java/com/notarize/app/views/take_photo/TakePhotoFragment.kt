package com.notarize.app.views.take_photo

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.notarize.app.R
import com.notarize.app.ext.createFile
import com.notarize.app.ext.createPhotosDirIfDoesntExist
import kotlinx.android.synthetic.main.fragment_photo.*
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class TakePhotoFragment : Fragment() {

    private val executor: Executor by lazy { Executors.newSingleThreadExecutor() }

    private var imageCapture: ImageCapture? = null
    private var lensFacing = CameraX.LensFacing.BACK

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
        requestPermissions()

        bt_take_photo.setOnClickListener {
            savePictureToFile()
        }
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
            setLensFacing(lensFacing)
            setTargetRotation(textureView.display.rotation)

        }.build()

        return Preview(previewConfig)
    }

    private fun createCaptureUseCase(): ImageCapture {
        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .apply {
                setLensFacing(lensFacing)
                setTargetRotation(textureView.display.rotation)
                setCaptureMode(ImageCapture.CaptureMode.MAX_QUALITY)
            }

//        applyExtensions(imageCaptureConfig)
        return ImageCapture(imageCaptureConfig.build())
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

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMetadata() = ImageCapture.Metadata().apply {
        isReversedHorizontal = lensFacing == CameraX.LensFacing.FRONT
    }

    private fun savePictureToFile() {
        requireContext().createPhotosDirIfDoesntExist()
        val file = requireContext().createFile()

        imageCapture?.takePicture(
            file,
            getMetadata(),
            executor,
            object : ImageCapture.OnImageSavedListener {
                override fun onImageSaved(file: File) {
                    val uri =
                        FileProvider.getUriForFile(
                            requireContext(),
                            requireContext().packageName,
                            file
                        )

                    Log.d("TakePhoto", "URI is ${uri.toString()}")
                }

                override fun onError(
                    imageCaptureError: ImageCapture.ImageCaptureError,
                    message: String,
                    cause: Throwable?
                ) {
                    Log.e("TakePhoto", message, cause)
                }

            })
    }
}