package GaitVision.com

import GaitVision.com.databinding.ActivityMainBinding
import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private var videoUri: Uri?=null
    private val REQUESTCODE_CAMERA=1
    private val REQUESTCODE_GALLERY=2

    private val REQUEST_CODE_PERMISSIONS = 101

    private fun checkPermissions() {
        // Permissions you want to request
        val permissions = arrayOf(
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.READ_MEDIA_VIDEO,
            android.Manifest.permission.READ_MEDIA_AUDIO
        )

        // Check if all permissions are granted
        if (!hasPermissions(*permissions)) {
            // Request permissions
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSIONS)
        } else {
            // Permissions already granted, proceed with your task
            proceedWithMediaAccess()
        }
    }

    // Function to check if all permissions are granted
    private fun hasPermissions(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun proceedWithMediaAccess() {
        startIntentFromGallary()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // All permissions granted, proceed with your task
                proceedWithMediaAccess()
            } else {
                // Permissions denied, show a message to the user
                Toast.makeText(this, "Permissions are required to access media files.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val getResult: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                videoUri = it

                val intent= Intent(this, SecondActivity::class.java).apply {
                    putExtra("VIDEO_URI", videoUri.toString())
                }
                startActivity(intent)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val confirmVidBtn = findViewById<Button>(R.id.confirm_vid_btn)
//        confirmVidBtn.setOnClickListener{
//            val intent = Intent(this, SecondActivity::class.java)
//            startActivity(intent)
//        }
        checkPermissions()
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        //mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.confirmVidBtn.setOnClickListener{startActivity(Intent(this,SecondActivity::class.java))}
        mBinding.openGalBtn.setOnClickListener{startIntentFromGallary()}

        val help01Btn = findViewById<Button>(R.id.help01_btn)
        help01Btn.setOnClickListener{
            val dialogBinding = layoutInflater.inflate(R.layout.help_dialog, null)

            val myDialog = Dialog(this)
            myDialog.setContentView(dialogBinding)

            myDialog.setCancelable(false)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()

            val yesBtn = dialogBinding.findViewById<Button>(R.id.help_yes)
            yesBtn.setOnClickListener{
                myDialog.dismiss()
            }

        }
        
    }

    private fun initClicks()
    {
        mBinding.openGalBtn.setOnClickListener{
            startIntentFromGallary()
        }
    }

    private fun startIntentFromGallary() {
       getResult.launch("video/*")
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
//    {
//        super.onActivityResult(requestCode, resultCode, data)
//
//            if(requestCode == REQUESTCODE_GALLERY && resultCode == Activity.RESULT_OK)
//            {
//                videoUri = data?.data
//
//                val intent= Intent(this, SecondActivity::class.java).apply{
//                    putExtra("VIDEO_URI", videoUri.toString())
//                }
//                startActivity(intent)
//            }
//
//
//    }
}
