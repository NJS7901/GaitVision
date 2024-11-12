package GaitVision.com

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.camera.core.*
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import GaitVision.com.databinding.ActivityFourthBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs

class FourthActivity : AppCompatActivity() {
    private val multiplePermissionId = 14
    private val mainBinding: ActivityFourthBinding by lazy {
        ActivityFourthBinding.inflate(layoutInflater)
    }

    private val multiplePermissionNameList = if (Build.VERSION.SDK_INT >= 33) {
        arrayListOf(android.Manifest.permission.CAMERA)
    } else {
        arrayListOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }

    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var cameraSelector: CameraSelector
    private var lensFacing = CameraSelector.LENS_FACING_BACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        if (checkMultiplePermission()) {
            startCamera()
        }

        mainBinding.flipCameraIB.setOnClickListener {
            lensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                CameraSelector.LENS_FACING_BACK
            } else {
                CameraSelector.LENS_FACING_FRONT
            }
            startCamera() // Restart camera with new lens facing
        }

        mainBinding.captureIB.setOnClickListener {
            takePhoto()
        }

        mainBinding.flashToggleIB.setOnClickListener {
            setFlashIcon(camera)
        }
    }

    private fun checkMultiplePermission(): Boolean {
        val listPermissionNeeded = arrayListOf<String>()
        for (permission in multiplePermissionNameList) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                listPermissionNeeded.add(permission)
            }
        }
        if (listPermissionNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionNeeded.toTypedArray(),
                multiplePermissionId
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

        if (requestCode == multiplePermissionId) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                startCamera()
            } else {
                appSettingOpen(this) // Define this function if needed
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUserCases()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = maxOf(width, height).toDouble() / minOf(width, height)
        return if (abs(previewRatio - 4.0 / 3.0) <= abs(previewRatio - 16.0 / 9.0)) {
            AspectRatio.RATIO_4_3
        } else {
            AspectRatio.RATIO_16_9
        }
    }

    private fun bindCameraUserCases() {
        val screenAspectRatio = aspectRatio(
            mainBinding.previewView.width,
            mainBinding.previewView.height
        )
        val rotation = mainBinding.previewView.display.rotation

        val resolutionSelector = ResolutionSelector.Builder()
            .setAspectRatioStrategy(
                AspectRatioStrategy(
                    screenAspectRatio,
                    AspectRatioStrategy.FALLBACK_RULE_AUTO
                )
            )
            .build()

        val preview = Preview.Builder()
            .setResolutionSelector(resolutionSelector)
            .setTargetRotation(rotation)
            .build()
            .also {
                it.setSurfaceProvider(mainBinding.previewView.surfaceProvider)
            }

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setResolutionSelector(resolutionSelector)
            .setTargetRotation(rotation)
            .build()

        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()

        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setFlashIcon(camera: Camera) {
        if (camera.cameraInfo.hasFlashUnit()) {
            val torchState = camera.cameraInfo.torchState.value ?: TorchState.OFF
            val isTorchOn = torchState == TorchState.ON
            camera.cameraControl.enableTorch(!isTorchOn)
            mainBinding.flashToggleIB.setImageResource(
                if (isTorchOn) R.drawable.bulb_24 else R.drawable.bulb_24
            )
        } else {
            Toast.makeText(this, "Flash is Not Available", Toast.LENGTH_LONG).show()
            mainBinding.flashToggleIB.isEnabled = false
        }
    }

    private fun takePhoto() {
        val imageFolder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Images")
        if (!imageFolder.exists()) {
            imageFolder.mkdir()
        }

        val filename = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .format(System.currentTimeMillis()) + ".jpg"
        val imageFile = File(imageFolder, filename)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(imageFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val message = "Photo Capture Succeeded: ${output.savedUri ?: imageFile.absolutePath}"
                    Toast.makeText(this@FourthActivity, message, Toast.LENGTH_LONG).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@FourthActivity, exception.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
        )
    }
}
